package com.thejohnsondev.datastore

import com.thejohnsondev.model.settings.DarkThemeConfig
import com.thejohnsondev.model.settings.GeneralSettings
import com.thejohnsondev.model.settings.PrivacySettings
import com.thejohnsondev.model.settings.ThemeBrand


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

    fun isFirstPasswordsLoad(): Boolean
    suspend fun setIsFirstPasswordsLoad(isFirstPasswordsLoad: Boolean)
    fun isFirstNotesLoad(): Boolean
    suspend fun setIsFirstNotesLoad(isFirstNotesLoad: Boolean)

    suspend fun setCustomTheme(theme: ThemeBrand)
    fun getCustomTheme(): ThemeBrand

    suspend fun setUseDynamicColor(useDynamicColor: Boolean)
    fun getUseDynamicColor(): Boolean

    suspend fun setDarkThemeConfig(config: DarkThemeConfig)
    fun getDarkThemeConfig(): DarkThemeConfig

    suspend fun setGeneralSettings(generalSettings: GeneralSettings)
    fun getGeneralSettings(): GeneralSettings

    suspend fun setPrivacySettings(privacySettings: PrivacySettings)
    fun getPrivacySettings(): PrivacySettings

}