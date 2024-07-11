package com.thejohnsondev.network.remote_datasource

import arrow.core.Either
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.model.auth.AuthResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    suspend fun signUp(email: String, password: String): Flow<Either<ApiError, AuthResponse>>
    suspend fun singIn(email: String, password: String): Flow<Either<ApiError, AuthResponse>>

    fun getUserPasswords(): Flow<Either<ApiError, List<PasswordModel>>>
    fun createPassword(passwordModel: PasswordModel): Flow<Either<ApiError, PasswordModel>>
    fun updatePassword(passwordModel: PasswordModel): Flow<Either<ApiError, PasswordModel>>
    fun updatePasswordsList(newPasswordList: List<PasswordModel>): Flow<DatabaseResponse>
    fun deletePassword(passwordId: String): Flow<Either<ApiError, Unit>>
    suspend fun deleteAccount(): Flow<Either<ApiError, Unit>>
    suspend fun changePassword(oldPassword: String, newPassword: String): Flow<Either<ApiError, Unit>>

    fun getNotes(): Flow<Either<ApiError, List<NoteModel>>>
    fun createNote(noteModel: NoteModel): Flow<Either<ApiError, NoteModel>>
    fun updateNote(noteModel: NoteModel): Flow<Either<ApiError, Unit>>
    fun deleteNote(noteId: String): Flow<Either<ApiError, Unit>>
}