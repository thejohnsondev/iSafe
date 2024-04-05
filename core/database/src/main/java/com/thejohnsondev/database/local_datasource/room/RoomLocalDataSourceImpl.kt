package com.thejohnsondev.database.local_datasource.room

import com.thejohnsondev.database.local_datasource.LocalDataSource
import com.thejohnsondev.database.local_datasource.room.dao.NotesDao
import com.thejohnsondev.database.local_datasource.room.dao.PasswordAdditionalFieldDao
import com.thejohnsondev.database.local_datasource.room.dao.PasswordsDao
import com.thejohnsondev.database.local_datasource.room.entity.AdditionalFieldEntity.Companion.toEntity
import com.thejohnsondev.database.local_datasource.room.entity.AdditionalFieldEntity.Companion.toModel
import com.thejohnsondev.database.local_datasource.room.entity.NoteEntity.Companion.toEntity
import com.thejohnsondev.database.local_datasource.room.entity.NoteEntity.Companion.toModel
import com.thejohnsondev.database.local_datasource.room.entity.PasswordEntity.Companion.toEntity
import com.thejohnsondev.database.local_datasource.room.entity.PasswordEntity.Companion.toModel
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.model.PasswordModel
import javax.inject.Inject

class RoomLocalDataSourceImpl @Inject constructor(
    private val passwordsDao: PasswordsDao,
    private val additionalFieldDao: PasswordAdditionalFieldDao,
    private val notesDao: NotesDao
) : LocalDataSource {
    override suspend fun getUserPasswords(): List<PasswordModel> =
        passwordsDao.getAllPasswords().map { passwordEntity ->
            passwordEntity.toModel().copy(
                additionalFields = additionalFieldDao
                    .getAdditionalFieldsByPasswordId(passwordEntity.id).map {
                        it.toModel()
                    }
            )
        }

    override suspend fun createPassword(passwordModel: PasswordModel): PasswordModel {
        passwordsDao.insertPassword(passwordModel.toEntity())
        passwordModel.additionalFields.forEach {
            additionalFieldDao.insertAdditionalField(it.toEntity(passwordModel.id))
        }
        return passwordModel
    }

    override suspend fun updatePassword(passwordModel: PasswordModel) {
        passwordsDao.updatePassword(passwordModel.toEntity())
        additionalFieldDao.deleteAdditionalFieldsByPasswordId(passwordModel.id)
        passwordModel.additionalFields.forEach {
            additionalFieldDao.insertAdditionalField(it.toEntity(passwordModel.id))
        }
    }

    override suspend fun updatePasswordsList(newPasswordList: List<PasswordModel>) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePassword(passwordId: String) {
        passwordsDao.deletePasswordById(passwordId)
        additionalFieldDao.deleteAdditionalFieldsByPasswordId(passwordId)
    }

    override suspend fun logout() {
        passwordsDao.deleteAllPasswords()
        additionalFieldDao.deleteAllAdditionalFields()
        notesDao.deleteAllNotes()
    }

    override suspend fun updatePasswords(passwords: List<PasswordModel>) {
        passwords.forEach {
            passwordsDao.updatePassword(it.toEntity())
        }
    }

    override suspend fun getNotes(): List<NoteModel> {
        return notesDao.getAllNotes().map { it.toModel() }
    }

    override suspend fun createNote(noteModel: NoteModel): NoteModel {
        notesDao.insertNote(noteModel.toEntity())
        return noteModel
    }

    override suspend fun updateNote(noteModel: NoteModel) {
        notesDao.updateNote(noteModel.toEntity())
    }

    override suspend fun deleteNote(noteId: String) {
        notesDao.deleteNoteById(noteId)
    }

    override suspend fun updateNotes(notes: List<NoteModel>) {
        notes.forEach {
            notesDao.insertNote(it.toEntity())
        }
    }
}