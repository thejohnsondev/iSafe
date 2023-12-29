package com.thejohnsondev.data

import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.model.UserPasswordsResponse
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PasswordsRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : PasswordsRepository {

    override fun getUserPasswords(userId: String): Flow<UserPasswordsResponse> =
        remoteDataSource.getUserPasswords(userId)

    override fun createPassword(userId: String, password: PasswordModel): Flow<DatabaseResponse> =
        remoteDataSource.createPassword(userId, password)

    override fun updatePassword(userId: String, password: PasswordModel): Flow<DatabaseResponse> =
        remoteDataSource.updatePassword(userId, password)

    override fun updatePasswordsList(
        userId: String,
        newPasswordList: List<PasswordModel>
    ): Flow<DatabaseResponse> =
        remoteDataSource.updatePasswordsList(userId, newPasswordList)

    override fun deletePassword(userId: String, passwordId: String): Flow<DatabaseResponse> =
        remoteDataSource.deletePassword(userId, passwordId)
}