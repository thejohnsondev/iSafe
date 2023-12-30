package com.thejohnsondev.data

import com.thejohnsondev.model.AuthResponse
import com.thejohnsondev.network.di.FirebaseRemoteDataSource
import com.thejohnsondev.network.di.NetworkModule
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @FirebaseRemoteDataSource private val remoteDataSource: RemoteDataSource
) : AuthRepository {
    override suspend fun signUp(email: String, password: String): Flow<AuthResponse> =
        remoteDataSource.signUp(email, password)

    override suspend fun singIn(email: String, password: String): Flow<AuthResponse> =
        remoteDataSource.singIn(email, password)

    override suspend fun signOut() {
        remoteDataSource.signOut()
    }

    override suspend fun getCurrentUserName(): Flow<String> =
        remoteDataSource.getCurrentUserName()

    override suspend fun getCurrentUserId(): Flow<String> =
        remoteDataSource.getCurrentUserId()

    override fun isUserLoggedIn(): Boolean = remoteDataSource.isUserLoggedIn()
}