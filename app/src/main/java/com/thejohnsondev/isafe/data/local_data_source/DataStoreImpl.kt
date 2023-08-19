package com.thejohnsondev.isafe.data.local_data_source

import android.content.Context
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.thejohnsondev.isafe.domain.models.UserModel
import com.thejohnsondev.isafe.utils.DATA_STORE_NAME
import com.thejohnsondev.isafe.utils.EMPTY
import com.thejohnsondev.isafe.utils.fromJson
import com.thejohnsondev.isafe.utils.toJson
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

    private fun DataStore.getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    override suspend fun saveUserData(userModel: UserModel) {
        putString(USER_DATA, userModel.toJson())
    }

    override suspend fun saveUserSecret(userSecret: String) {
        val currentSavedUserData: UserModel = getString(USER_DATA, EMPTY).fromJson()
        putString(USER_DATA, currentSavedUserData.copy(userSecret = userSecret).toJson())
    }

    override suspend fun saveUserKey(byteArray: ByteArray) {
        val encodedKey = Base64.encodeToString(byteArray, Base64.NO_WRAP)
        putString(USER_KEY, encodedKey)
    }

    override suspend fun getUserData(): UserModel {
        return getString(USER_DATA, EMPTY).fromJson()
    }

    override suspend fun getUserSecret(): String? {
        val userData: UserModel = getString(USER_DATA, EMPTY).fromJson()
        return userData.userSecret
    }

    override suspend fun getUserKey(): ByteArray {
        val encodedKey = getString(USER_KEY, EMPTY)
        return Base64.decode(encodedKey, Base64.NO_WRAP)
    }

    companion object {
        private const val USER_KEY = "user_key"
        private const val USER_DATA = "user_data"
    }
}