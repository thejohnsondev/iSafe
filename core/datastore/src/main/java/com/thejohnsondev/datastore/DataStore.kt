package com.thejohnsondev.datastore

import com.thejohnsondev.model.UserModel

interface DataStore {
    suspend fun saveUserData(userModel: UserModel)
    suspend fun saveUserKey(byteArray: ByteArray)
    suspend fun setIsFromLogin(isFromLogin: Boolean)

    suspend fun getUserData(): UserModel
    suspend fun getUserKey(): ByteArray
    suspend fun getIsFromLogin(): Boolean

    suspend fun clearUserData()
}