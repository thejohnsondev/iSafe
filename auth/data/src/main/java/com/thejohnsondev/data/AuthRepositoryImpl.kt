package com.thejohnsondev.data

import arrow.core.Either
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.auth.AuthResponse
import com.thejohnsondev.network.di.DotNetRemoteDataSource
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @DotNetRemoteDataSource private val remoteDataSource: RemoteDataSource
) : AuthRepository {
    override suspend fun signUp(email: String, password: String): Flow<Either<ApiError, AuthResponse>> =
        remoteDataSource.signUp(email, password)

    override suspend fun singIn(email: String, password: String): Flow<Either<ApiError, AuthResponse>> =
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