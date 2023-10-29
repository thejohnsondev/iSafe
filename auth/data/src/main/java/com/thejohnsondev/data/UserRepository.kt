package com.thejohnsondev.data

import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.UserDataResponse
import com.thejohnsondev.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun createUser(userModel: UserModel): Flow<DatabaseResponse>
    fun updateUserSecret(userId: String, userSecret: String): Flow<DatabaseResponse>
    fun getUserData(userId: String): Flow<UserDataResponse>
}