package com.thejohnsondev.datastore


interface DataStore {
    suspend fun getUserKey(): ByteArray
    suspend fun saveUserKey(byteArray: ByteArray)

    suspend fun saveUserEmail(email: String)
    fun getUserEmail(): String

    fun getBaseUrl(): String
    suspend fun setBaseUrl(baseUrl: String)
    fun getUserToken(): String
    suspend fun saveUserToken(token: String)

    suspend fun clearUserData()

    fun isUserLoggedIn(): Boolean
}