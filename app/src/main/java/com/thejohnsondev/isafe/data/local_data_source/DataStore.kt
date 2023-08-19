package com.thejohnsondev.isafe.data.local_data_source

import android.service.autofill.UserData
import com.thejohnsondev.isafe.domain.models.UserModel

interface DataStore {
    suspend fun saveUserData(userModel: UserModel)
    suspend fun saveUserSecret(userSecret: String)
    suspend fun saveUserKey(byteArray: ByteArray)

    suspend fun getUserData(): UserData
    suspend fun getUserSecret(): String?
    suspend fun getUserKey(): ByteArray
}