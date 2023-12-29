package com.thejohnsondev.network.remote_datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.thejohnsondev.model.AuthResponse
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.model.UserDataResponse
import com.thejohnsondev.model.UserModel
import com.thejohnsondev.model.UserPasswordsResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FirebaseRemoteDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
): RemoteDataSource {
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

    override fun createUser(userModel: UserModel): Flow<DatabaseResponse> {
        TODO("Not yet implemented")
    }

    override fun updateUserSecret(userId: String, userSecret: String): Flow<DatabaseResponse> {
        TODO("Not yet implemented")
    }

    override fun getUserData(userId: String): Flow<UserDataResponse> {
        TODO("Not yet implemented")
    }

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
}