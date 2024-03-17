package com.thejohnsondev.data

import arrow.core.Either
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.network.di.DotNetRemoteDataSource
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PasswordsRepositoryImpl @Inject constructor(
    @DotNetRemoteDataSource private val remoteDataSource: RemoteDataSource,
) : PasswordsRepository {

    override fun getUserPasswords(userId: String): Flow<Either<ApiError, List<PasswordModel>>> =
        remoteDataSource.getUserPasswords(userId)

    override fun createPassword(passwordId: String, password: PasswordModel): Flow<Either<ApiError, PasswordModel>> =
        remoteDataSource.createPassword(passwordId, password)

    override fun updatePassword(passwordId: String, password: PasswordModel): Flow<Either<ApiError, Unit>> =
        remoteDataSource.updatePassword(passwordId, password)

    override fun updatePasswordsList(
        userId: String,
        newPasswordList: List<PasswordModel>
    ): Flow<DatabaseResponse> =
        remoteDataSource.updatePasswordsList(userId, newPasswordList)

    override fun deletePassword(userId: String, passwordId: String): Flow<Either<ApiError, Unit>> =
        remoteDataSource.deletePassword(userId, passwordId)
}