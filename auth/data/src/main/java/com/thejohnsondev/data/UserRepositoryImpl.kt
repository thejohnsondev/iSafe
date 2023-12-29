package com.thejohnsondev.data

import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.UserDataResponse
import com.thejohnsondev.model.UserModel
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : UserRepository {

    override fun createUser(userModel: UserModel): Flow<DatabaseResponse> =
        remoteDataSource.createUser(userModel)

    override fun updateUserSecret(userId: String, userSecret: String): Flow<DatabaseResponse> =
        remoteDataSource.updateUserSecret(userId, userSecret)

    override fun getUserData(userId: String): Flow<UserDataResponse> =
        remoteDataSource.getUserData(userId)

}