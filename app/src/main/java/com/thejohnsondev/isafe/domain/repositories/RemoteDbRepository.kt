package com.thejohnsondev.isafe.domain.repositories

import com.thejohnsondev.isafe.domain.models.DatabaseResponse
import com.thejohnsondev.isafe.domain.models.UserDataResponse
import com.thejohnsondev.isafe.domain.models.UserModel
import kotlinx.coroutines.flow.Flow

interface RemoteDbRepository {
    fun createUser(userModel: UserModel): Flow<DatabaseResponse>
    fun updateUserSecret(userId: String, userSecret: String): Flow<DatabaseResponse>
    fun getUserData(userId: String): Flow<UserDataResponse>
}