package com.thejohnsondev.data

import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.UserDataResponse
import com.thejohnsondev.model.UserModel
import com.thejohnsondev.network.di.FirebaseRemoteDataSource
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    @FirebaseRemoteDataSource private val remoteDataSource: RemoteDataSource
) : UserRepository {

    override fun createUser(userModel: UserModel): Flow<DatabaseResponse> =
        remoteDataSource.createUser(userModel)

    override fun getUserData(userId: String): Flow<UserDataResponse> =
        remoteDataSource.getUserData(userId)

}