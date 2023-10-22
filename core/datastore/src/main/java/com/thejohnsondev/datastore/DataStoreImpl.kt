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

    override suspend fun saveUserData(userModel: UserModel) {
        putString(USER_DATA, userModel.toJson())
    }

    override suspend fun saveUserKey(byteArray: ByteArray) {
        val encodedKey = Base64.encodeToString(byteArray, Base64.NO_WRAP)
        putString(USER_KEY, encodedKey)
    }

    override suspend fun setIsFromLogin(isFromLogin: Boolean) {
        putBoolean(IS_FROM_LOGIN, isFromLogin)
    }

    override suspend fun getUserData(): UserModel {
        return getString(USER_DATA, EMPTY).fromJson()
    }

    override suspend fun getUserKey(): ByteArray {
        val encodedKey = getString(USER_KEY, EMPTY)
        return Base64.decode(encodedKey, Base64.NO_WRAP)
    }

    override suspend fun getIsFromLogin(): Boolean {
        return getBoolean(IS_FROM_LOGIN)
    }

    override suspend fun clearUserData() {
        remove(USER_KEY)
        remove(USER_DATA)
        remove(IS_FROM_LOGIN)
    }

    companion object {
        private const val USER_KEY = "user_key"
        private const val USER_DATA = "user_data"
        private const val IS_FROM_LOGIN = "is-from-login"
    }
}