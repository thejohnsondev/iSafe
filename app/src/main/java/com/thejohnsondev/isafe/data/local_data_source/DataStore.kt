package com.thejohnsondev.isafe.data.local_data_source

import android.service.autofill.UserData
import com.thejohnsondev.isafe.domain.models.UserModel

interface DataStore {
    suspend fun saveUserData(userModel: UserModel)
    suspend fun saveUserKey(byteArray: ByteArray)
    suspend fun setIsFromLogin(isFromLogin: Boolean)

    suspend fun getUserData(): UserModel
    suspend fun getUserKey(): ByteArray
    suspend fun getIsFromLogin(): Boolean

    suspend fun clearUserData()
}