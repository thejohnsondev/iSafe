package com.thejohnsondev.data

import arrow.core.Either
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.model.UserPasswordsResponse
import com.thejohnsondev.network.di.DotNetRemoteDataSource
import com.thejohnsondev.network.di.FirebaseRemoteDataSource
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PasswordsRepositoryImpl @Inject constructor(
    @DotNetRemoteDataSource private val remoteDataSource: RemoteDataSource,
) : PasswordsRepository {

    override fun getUserPasswords(userId: String): Flow<Either<ApiError, List<PasswordModel>>> =
        remoteDataSource.getUserPasswords(userId)

    override fun createPassword(userId: String, password: PasswordModel): Flow<Either<ApiError, PasswordModel>> =
        remoteDataSource.createPassword(userId, password)

    override fun updatePassword(userId: String, password: PasswordModel): Flow<Either<ApiError, Unit>> =
        remoteDataSource.updatePassword(userId, password)

    override fun updatePasswordsList(
        userId: String,
        newPasswordList: List<PasswordModel>
    ): Flow<DatabaseResponse> =
        remoteDataSource.updatePasswordsList(userId, newPasswordList)

    override fun deletePassword(userId: String, passwordId: String): Flow<Either<ApiError, Unit>> =
        remoteDataSource.deletePassword(userId, passwordId)
}