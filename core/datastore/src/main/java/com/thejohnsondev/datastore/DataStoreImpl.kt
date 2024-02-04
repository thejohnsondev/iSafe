package com.thejohnsondev.datastore

import android.content.Context
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.thejohnsondev.common.DATA_STORE_NAME
import com.thejohnsondev.common.EMPTY
import com.thejohnsondev.common.fromJson
import com.thejohnsondev.common.toJson
import com.thejohnsondev.model.UserModel
import javax.inject.Inject

class DataStoreImpl @Inject constructor(
    applicationContext: Context
) : DataStore {

    private val sharedPrefFile = DATA_STORE_NAME
    private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    private val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
    private val sharedPreferences = EncryptedSharedPreferences.create(
        sharedPrefFile,
        mainKeyAlias,
        applicationContext,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private fun DataStore.putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    private fun DataStore.putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    private fun DataStore.getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    private fun DataStore.getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    private fun DataStore.remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    override suspend fun saveUserKey(byteArray: ByteArray) {
        val encodedKey = Base64.encodeToString(byteArray, Base64.NO_WRAP)
        putString(USER_KEY, encodedKey)
    }

    override suspend fun getUserKey(): ByteArray {
        val encodedKey = getString(USER_KEY, EMPTY)
        return Base64.decode(encodedKey, Base64.NO_WRAP)
    }

    override fun getBaseUrl(): String {
        return getString(BASE_URL, DEFAULT_BASE_URL)
    }

    override suspend fun setBaseUrl(baseUrl: String) {
        putString(BASE_URL, baseUrl)
    }

    override fun getUserToken(): String {
        return getString(USER_TOKEN, EMPTY)
    }

    override suspend fun saveUserToken(token: String) {
        putString(USER_TOKEN, token)
    }

    override suspend fun clearUserToken() {
        remove(USER_TOKEN)
    }

    override fun isUserLoggedIn(): Boolean {
        return getUserToken().isNotEmpty()
    }

    companion object {
        private const val USER_KEY = "user_key"
        private const val USER_DATA = "user_data"
        private const val IS_FROM_LOGIN = "is-from-login"
        private const val USER_TOKEN = "user-token"
        private const val BASE_URL = "base-url"
        private const val DEFAULT_BASE_URL = "https://isafeapi2.azurewebsites.net/"
    }
}