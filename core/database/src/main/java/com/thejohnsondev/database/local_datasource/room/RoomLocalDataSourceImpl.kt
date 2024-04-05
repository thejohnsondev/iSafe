package com.thejohnsondev.database.local_datasource.room

import arrow.core.Either
import com.thejohnsondev.common.awaitChannelFlow
import com.thejohnsondev.common.sendOrNothing
import com.thejohnsondev.database.local_datasource.LocalDataSource
import com.thejohnsondev.database.local_datasource.room.db.ISafeDataBase
import com.thejohnsondev.database.local_datasource.room.entity.AdditionalFieldEntity.Companion.toEntity
import com.thejohnsondev.database.local_datasource.room.entity.AdditionalFieldEntity.Companion.toModel
import com.thejohnsondev.database.local_datasource.room.entity.NoteEntity.Companion.toEntity
import com.thejohnsondev.database.local_datasource.room.entity.NoteEntity.Companion.toModel
import com.thejohnsondev.database.local_datasource.room.entity.PasswordEntity.Companion.toEntity
import com.thejohnsondev.database.local_datasource.room.entity.PasswordEntity.Companion.toModel
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.model.PasswordModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomLocalDataSourceImpl @Inject constructor(
    private val iSafeDataBase: ISafeDataBase
) : LocalDataSource {
    override suspend fun getUserPasswords(): List<PasswordModel> =
        iSafeDataBase.getPasswordsDao().getAllPasswords().map { passwordEntity ->
            passwordEntity.toModel().copy(
                additionalFields = iSafeDataBase.getAdditionalFieldsDao()
                    .getAdditionalFieldsByPasswordId(passwordEntity.id).map {
                        it.toModel()
                    }
            )
        }

    override suspend fun createPassword(passwordModel: PasswordModel): PasswordModel {
        iSafeDataBase.getPasswordsDao().insertPassword(passwordModel.toEntity())
        passwordModel.additionalFields.forEach {
            iSafeDataBase.getAdditionalFieldsDao()
                .insertAdditionalField(it.toEntity(passwordModel.id))
        }
        return passwordModel
    }

    override fun updatePassword(passwordModel: PasswordModel): Flow<Either<ApiError, Unit>> =
        awaitChannelFlow {
            iSafeDataBase.getPasswordsDao().updatePassword(passwordModel.toEntity())
            passwordModel.additionalFields.forEach {
                iSafeDataBase.getAdditionalFieldsDao()
                    .updateAdditionalField(it.toEntity(passwordModel.id))
            }
            sendOrNothing(Either.Right(Unit))
        }

    override fun updatePasswordsList(newPasswordList: List<PasswordModel>): Flow<DatabaseResponse> {
        TODO("Not yet implemented")
    }

    override fun deletePassword(passwordId: String): Flow<Either<ApiError, Unit>> =
        awaitChannelFlow {
            iSafeDataBase.getAdditionalFieldsDao().getAdditionalFieldsByPasswordId(passwordId)
                .forEach {
                    iSafeDataBase.getAdditionalFieldsDao().deletePasswordAdditionalFieldById(it.id)
                }
            iSafeDataBase.getPasswordsDao().deletePasswordById(passwordId)
            sendOrNothing(Either.Right(Unit))
        }

    override fun deleteAccount(): Flow<Either<ApiError, Unit>> = awaitChannelFlow {
        iSafeDataBase.getAdditionalFieldsDao().deleteAllAdditionalFields()
        iSafeDataBase.getPasswordsDao().deleteAllPasswords()
        iSafeDataBase.getNotesDao().deleteAllNotes()
        sendOrNothing(Either.Right(Unit))
    }

    override fun getNotes(): Flow<Either<ApiError, List<NoteModel>>> = awaitChannelFlow {
        sendOrNothing(Either.Right(iSafeDataBase.getNotesDao().getAllNotes().map { it.toModel() }))
    }

    override fun createNote(noteModel: NoteModel): Flow<Either<ApiError, NoteModel>> =
        awaitChannelFlow {
            iSafeDataBase.getNotesDao().insertNote(noteModel.toEntity())
            sendOrNothing(Either.Right(noteModel))
        }

    override fun updateNote(noteModel: NoteModel): Flow<Either<ApiError, Unit>> = awaitChannelFlow {
        sendOrNothing(Either.Right(iSafeDataBase.getNotesDao().updateNote(noteModel.toEntity())))
    }

    override fun deleteNote(noteId: String): Flow<Either<ApiError, Unit>> = awaitChannelFlow {
        sendOrNothing(Either.Right(iSafeDataBase.getNotesDao().deleteNoteById(noteId)))
    }
}