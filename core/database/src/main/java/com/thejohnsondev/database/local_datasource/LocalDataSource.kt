package com.thejohnsondev.database.local_datasource

import arrow.core.Either
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.model.PasswordModel
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun getUserPasswords(): List<PasswordModel>
    suspend fun createPassword(passwordModel: PasswordModel): PasswordModel
    fun updatePassword(passwordModel: PasswordModel): Flow<Either<ApiError, Unit>>
    fun updatePasswordsList(newPasswordList: List<PasswordModel>): Flow<DatabaseResponse>
    fun deletePassword(passwordId: String): Flow<Either<ApiError, Unit>>
    fun deleteAccount(): Flow<Either<ApiError, Unit>>

    fun getNotes(): Flow<Either<ApiError, List<NoteModel>>>
    fun createNote(noteModel: NoteModel): Flow<Either<ApiError, NoteModel>>
    fun updateNote(noteModel: NoteModel): Flow<Either<ApiError, Unit>>
    fun deleteNote(noteId: String): Flow<Either<ApiError, Unit>>

}