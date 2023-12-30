package com.thejohnsondev.network.remote_datasource.dotnet

import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.model.AuthResponse
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.model.UserDataResponse
import com.thejohnsondev.model.UserModel
import com.thejohnsondev.model.UserNotesResponse
import com.thejohnsondev.model.UserPasswordsResponse
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ISafeDotNetRemoteDataSource @Inject constructor(
    private val api: ISafeDotNetApi,
    private val coroutineScope: CoroutineScope,
    private val dataStore: DataStore
): RemoteDataSource {
    override suspend fun signUp(email: String, password: String): Flow<AuthResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun singIn(email: String, password: String): Flow<AuthResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentUserName(): Flow<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentUserId(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun isUserLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }

    override fun createUser(userModel: UserModel): Flow<DatabaseResponse> {
        TODO("Not yet implemented")
    }

    override fun updateUserSecret(userId: String, userSecret: String): Flow<DatabaseResponse> {
        TODO("Not yet implemented")
    }

    override fun getUserData(userId: String): Flow<UserDataResponse> {
        TODO("Not yet implemented")
    }

    override fun getUserPasswords(userId: String): Flow<UserPasswordsResponse> {
        TODO("Not yet implemented")
    }

    override fun createPassword(userId: String, password: PasswordModel): Flow<DatabaseResponse> {
        TODO("Not yet implemented")
    }

    override fun updatePassword(userId: String, password: PasswordModel): Flow<DatabaseResponse> {
        TODO("Not yet implemented")
    }

    override fun updatePasswordsList(
        userId: String,
        newPasswordList: List<PasswordModel>
    ): Flow<DatabaseResponse> {
        TODO("Not yet implemented")
    }

    override fun deletePassword(userId: String, passwordId: String): Flow<DatabaseResponse> {
        TODO("Not yet implemented")
    }

    override fun getUserNotes(userId: String): Flow<UserNotesResponse> {
        TODO("Not yet implemented")
    }

    override fun createNote(userId: String, note: NoteModel): Flow<DatabaseResponse> {
        TODO("Not yet implemented")
    }

    override fun updateNote(userId: String, note: NoteModel): Flow<DatabaseResponse> {
        TODO("Not yet implemented")
    }

    override fun deleteNote(userId: String, noteId: Int): Flow<DatabaseResponse> {
        TODO("Not yet implemented")
    }
}