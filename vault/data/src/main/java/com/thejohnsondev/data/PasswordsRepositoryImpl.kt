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

    override fun getUserPasswords(): Flow<Either<ApiError, List<PasswordModel>>> =
        remoteDataSource.getUserPasswords()

    override fun createPassword(password: PasswordModel): Flow<Either<ApiError, PasswordModel>> =
        remoteDataSource.createPassword(password)

    override fun updatePassword(password: PasswordModel): Flow<Either<ApiError, Unit>> =
        remoteDataSource.updatePassword(password)

    override fun updatePasswordsList(
        newPasswordList: List<PasswordModel>
    ): Flow<DatabaseResponse> =
        remoteDataSource.updatePasswordsList(newPasswordList)

    override fun deletePassword(passwordId: String): Flow<Either<ApiError, Unit>> =
        remoteDataSource.deletePassword(passwordId)
}