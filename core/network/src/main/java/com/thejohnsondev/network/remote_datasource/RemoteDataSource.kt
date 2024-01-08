package com.thejohnsondev.network.remote_datasource

import arrow.core.Either
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.model.UserDataResponse
import com.thejohnsondev.model.UserModel
import com.thejohnsondev.model.UserNotesResponse
import com.thejohnsondev.model.UserPasswordsResponse
import com.thejohnsondev.model.auth.AuthResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    suspend fun signUp(email: String, password: String): Flow<Either<ApiError, AuthResponse>>
    suspend fun singIn(email: String, password: String): Flow<Either<ApiError, AuthResponse>>
    suspend fun signOut()
    suspend fun getCurrentUserName(): Flow<String>
    suspend fun getCurrentUserId(): Flow<String>
    fun isUserLoggedIn(): Boolean

    fun createUser(userModel: UserModel): Flow<DatabaseResponse>
    fun getUserData(userId: String): Flow<UserDataResponse>

    fun getUserPasswords(userId: String): Flow<UserPasswordsResponse>
    fun createPassword(userId: String, password: PasswordModel): Flow<DatabaseResponse>
    fun updatePassword(userId: String, password: PasswordModel): Flow<DatabaseResponse>
    fun updatePasswordsList(userId: String, newPasswordList: List<PasswordModel>): Flow<DatabaseResponse>
    fun deletePassword(userId: String, passwordId: String): Flow<DatabaseResponse>

    fun getUserNotes(userId: String): Flow<UserNotesResponse>
    fun createNote(userId: String, note: NoteModel): Flow<DatabaseResponse>
    fun updateNote(userId: String, note: NoteModel): Flow<DatabaseResponse>
    fun deleteNote(userId: String, noteId: Int): Flow<DatabaseResponse>
}