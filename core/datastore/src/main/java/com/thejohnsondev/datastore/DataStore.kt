package com.thejohnsondev.datastore

import com.thejohnsondev.model.UserModel

interface DataStore {
    //    suspend fun getUserData(): UserModel
    suspend fun getUserKey(): ByteArray
    //    suspend fun saveUserData(userModel: UserModel)
    suspend fun saveUserKey(byteArray: ByteArray)

//    suspend fun clearUserData()

    fun getBaseUrl(): String
    suspend fun setBaseUrl(baseUrl: String)
    fun getUserToken(): String
    suspend fun saveUserToken(token: String)

    suspend fun clearUserToken()

    fun isUserLoggedIn(): Boolean
}