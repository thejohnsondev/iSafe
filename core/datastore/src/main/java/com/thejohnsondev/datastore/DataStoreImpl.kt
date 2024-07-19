package com.thejohnsondev.datastore

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.thejohnsondev.common.DATA_STORE_NAME
import com.thejohnsondev.common.EMPTY
import com.thejohnsondev.model.settings.DarkThemeConfig
import com.thejohnsondev.model.settings.GeneralSettings
import com.thejohnsondev.model.settings.PrivacySettings
import com.thejohnsondev.model.settings.ThemeBrand
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

    private fun DataStore.putInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    private fun DataStore.getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    private fun DataStore.remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    override suspend fun saveUserKey(byteArray: ByteArray) {
        val encodedKey = Base64.encodeToString(byteArray, Base64.NO_WRAP)
        putString(USER_KEY, encodedKey)
    }

    override suspend fun saveUserEmail(email: String) {
        putString(USER_EMAIL, email)
    }

    override fun getUserEmail(): String {
        return getString(USER_EMAIL, EMPTY)
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

    override suspend fun clearUserData() {
        remove(USER_KEY)
        remove(USER_EMAIL)
        remove(USER_TOKEN)
    }

    override fun isUserLoggedIn(): Boolean {
        return getUserToken().isNotEmpty()
    }

    override fun isFirstPasswordsLoad(): Boolean {
        return getBoolean(IS_FIRST_PASSWORDS_LOAD)
    }

    override suspend fun setIsFirstPasswordsLoad(isFirstPasswordsLoad: Boolean) {
        putBoolean(IS_FIRST_PASSWORDS_LOAD, isFirstPasswordsLoad)
    }

    override fun isFirstNotesLoad(): Boolean {
        return getBoolean(IS_FIRST_NOTES_LOAD)
    }

    override suspend fun setIsFirstNotesLoad(isFirstNotesLoad: Boolean) {
        putBoolean(IS_FIRST_NOTES_LOAD, isFirstNotesLoad)
    }

    override suspend fun setCustomTheme(theme: ThemeBrand) {
        putInt(THEME_BRAND, theme.ordinal)
    }

    override fun getCustomTheme(): ThemeBrand {
        val theme = getInt(THEME_BRAND, ThemeBrand.DEFAULT.ordinal)
        return when (theme) {
            ThemeBrand.DEFAULT.ordinal -> ThemeBrand.DEFAULT
            ThemeBrand.ANDROID.ordinal -> ThemeBrand.ANDROID
            else -> ThemeBrand.DEFAULT
        }
    }

    override suspend fun setUseDynamicColor(useDynamicColor: Boolean) {
        putBoolean(USE_DYNAMIC_COLOR, useDynamicColor)
    }

    override fun getUseDynamicColor(): Boolean {
        return getBoolean(USE_DYNAMIC_COLOR)
    }

    override suspend fun setDarkThemeConfig(config: DarkThemeConfig) {
        putInt(DARK_THEME_CONFIG, config.ordinal)
    }

    override fun getDarkThemeConfig(): DarkThemeConfig {
        val darkThemeConfig = getInt(DARK_THEME_CONFIG, DarkThemeConfig.SYSTEM.ordinal)
        return when (darkThemeConfig) {
            DarkThemeConfig.SYSTEM.ordinal -> DarkThemeConfig.SYSTEM
            DarkThemeConfig.LIGHT.ordinal -> DarkThemeConfig.LIGHT
            DarkThemeConfig.DARK.ordinal -> DarkThemeConfig.DARK
            else -> DarkThemeConfig.SYSTEM
        }
    }

    override suspend fun setGeneralSettings(generalSettings: GeneralSettings) {
        putBoolean(USE_DEEP_SEARCH, generalSettings.isDeepSearchEnabled)
    }

    override fun getGeneralSettings(): GeneralSettings {
        val isUseDeepSearch = getBoolean(USE_DEEP_SEARCH)
        return GeneralSettings(isDeepSearchEnabled = isUseDeepSearch)
    }

    override suspend fun setPrivacySettings(privacySettings: PrivacySettings) {
        putBoolean(UNLOCK_WITH_BIOMETRICS, privacySettings.isUnlockWithBiometricEnabled)
        putBoolean(BLOCK_SCREENSHOTS, privacySettings.isBlockScreenshotsEnabled)
    }

    override fun getPrivacySettings(): PrivacySettings {
        val isUnlockWithBiometrics = getBoolean(UNLOCK_WITH_BIOMETRICS)
        val isBlockScreenshots = getBoolean(BLOCK_SCREENSHOTS)
        return PrivacySettings(
            isUnlockWithBiometricEnabled = isUnlockWithBiometrics,
            isBlockScreenshotsEnabled = isBlockScreenshots
        )
    }

    companion object {
        private const val USER_KEY = "user_key"
        private const val USER_EMAIL = "user-email"
        private const val USER_TOKEN = "user-token"
        private const val BASE_URL = "base-url"
        private const val IS_FIRST_PASSWORDS_LOAD = "is-first-passwords-load"
        private const val IS_FIRST_NOTES_LOAD = "is-first-notes-load"
        private const val DEFAULT_BASE_URL = "https://isafeapi2.azurewebsites.net/"
        private const val THEME_BRAND = "theme-brand"
        private const val USE_DYNAMIC_COLOR = "use-dynamic-color"
        private const val DARK_THEME_CONFIG = "dark-theme-config"
        private const val USE_DEEP_SEARCH = "use-deep-search"
        private const val UNLOCK_WITH_BIOMETRICS = "unlock-with-biometrics"
        private const val BLOCK_SCREENSHOTS = "block-screenshots"
    }
}