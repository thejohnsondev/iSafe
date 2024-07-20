package com.thejohnsondev.data

import arrow.core.Either
import com.thejohnsondev.common.key_utils.KeyUtils
import com.thejohnsondev.database.local_datasource.LocalDataSource
import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.auth.AuthResponse
import com.thejohnsondev.network.di.DotNetRemoteDataSource
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @DotNetRemoteDataSource private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val dataStore: DataStore,
    private val keyUtils: KeyUtils
) : AuthRepository {
    override suspend fun signUp(email: String, password: String): Flow<Either<ApiError, AuthResponse>> {
        val encryptedEmail = keyUtils.encrypt(email, BuildConfig.AUTH_SECRET_KEY.toByteArray())
        val encryptedPassword = keyUtils.encrypt(password, BuildConfig.AUTH_SECRET_KEY.toByteArray())
        return remoteDataSource.signUp(encryptedEmail, encryptedPassword)
    }

    override suspend fun singIn(email: String, password: String): Flow<Either<ApiError, AuthResponse>> {
        val encryptedEmail = keyUtils.encrypt(email, BuildConfig.AUTH_SECRET_KEY.toByteArray())
        val encryptedPassword = keyUtils.encrypt(password, BuildConfig.AUTH_SECRET_KEY.toByteArray())
        return remoteDataSource.singIn(encryptedEmail, encryptedPassword)
    }


    override suspend fun signOut() {
        localDataSource.logout()
        dataStore.clearUserData()
        dataStore.setIsFirstPasswordsLoad(true)
        dataStore.setIsFirstNotesLoad(true)
    }

    override fun isUserLoggedIn(): Boolean = dataStore.isUserLoggedIn()
    override suspend fun deleteAccount(): Flow<Either<ApiError, Unit>> {
        return remoteDataSource.deleteAccount()
    }

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): Flow<Either<ApiError, Unit>> {
        return remoteDataSource.changePassword(
            keyUtils.encrypt(oldPassword, BuildConfig.AUTH_SECRET_KEY.toByteArray()),
            keyUtils.encrypt(newPassword, BuildConfig.AUTH_SECRET_KEY.toByteArray())
        )
    }
}