package com.thejohnsondev.data

import arrow.core.Either
import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.auth.AuthResponse
import com.thejohnsondev.network.di.DotNetRemoteDataSource
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @DotNetRemoteDataSource private val remoteDataSource: RemoteDataSource,
    private val dataStore: DataStore
) : AuthRepository {
    override suspend fun signUp(email: String, password: String): Flow<Either<ApiError, AuthResponse>> =
        remoteDataSource.signUp(email, password)

    override suspend fun singIn(email: String, password: String): Flow<Either<ApiError, AuthResponse>> =
        remoteDataSource.singIn(email, password)


    override suspend fun signOut() {
        dataStore.clearUserToken()
    }

    override fun isUserLoggedIn(): Boolean = dataStore.isUserLoggedIn()
    override fun deleteAccount(): Flow<Either<ApiError, Unit>> = remoteDataSource.deleteAccount()
}