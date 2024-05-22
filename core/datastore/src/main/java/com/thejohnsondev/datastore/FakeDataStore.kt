package com.thejohnsondev.datastore

import com.thejohnsondev.model.settings.DarkThemeConfig
import com.thejohnsondev.model.settings.GeneralSettings
import com.thejohnsondev.model.settings.PrivacySettings
import com.thejohnsondev.model.settings.ThemeBrand

class FakeDataStore : DataStore {

    private var userKey: ByteArray = ByteArray(0)
    private var userEmail: String = "test@email.com"
    private var baseUrl: String = ""
    private var userToken: String = ""
    private var userLoggedIn: Boolean = false
    private var firstPasswordsLoad: Boolean = false
    private var firstNotesLoad: Boolean = false
    private var customTheme: ThemeBrand = ThemeBrand.ANDROID
    private var useDynamicColor: Boolean = false
    private var darkThemeConfig: DarkThemeConfig = DarkThemeConfig.DARK
    private var generalSettings: GeneralSettings = GeneralSettings()
    private var privacySettings: PrivacySettings = PrivacySettings()


    override suspend fun getUserKey(): ByteArray = userKey

    override suspend fun saveUserKey(byteArray: ByteArray) {
        userKey = byteArray
    }

    override suspend fun saveUserEmail(email: String) {
        userEmail = email
    }

    override fun getUserEmail(): String {
        return userEmail
    }

    override fun getBaseUrl(): String {
        return baseUrl
    }

    override suspend fun setBaseUrl(baseUrl: String) {
        this.baseUrl = baseUrl
    }

    override fun getUserToken(): String {
        return userToken
    }

    override suspend fun saveUserToken(token: String) {
        this.userToken = token
    }

    override suspend fun clearUserData() {
        userKey = ByteArray(0)
        userEmail = ""
        baseUrl = ""
        userToken = ""
        userLoggedIn = false
        firstPasswordsLoad = false
        firstNotesLoad = false
        customTheme = ThemeBrand.ANDROID
        useDynamicColor = false
        darkThemeConfig = DarkThemeConfig.DARK
        generalSettings = GeneralSettings()
        privacySettings = PrivacySettings()
    }

    override fun isUserLoggedIn(): Boolean {
        return userLoggedIn
    }

    override fun isFirstPasswordsLoad(): Boolean {
        return firstPasswordsLoad
    }

    override suspend fun setIsFirstPasswordsLoad(isFirstPasswordsLoad: Boolean) {
        firstPasswordsLoad = isFirstPasswordsLoad
    }

    override fun isFirstNotesLoad(): Boolean {
        return firstNotesLoad
    }

    override suspend fun setIsFirstNotesLoad(isFirstNotesLoad: Boolean) {
        this.firstNotesLoad = isFirstNotesLoad
    }

    override suspend fun setCustomTheme(theme: ThemeBrand) {
        this.customTheme = theme
    }

    override fun getCustomTheme(): ThemeBrand {
        return customTheme
    }

    override suspend fun setUseDynamicColor(useDynamicColor: Boolean) {
        this.useDynamicColor = useDynamicColor
    }

    override fun getUseDynamicColor(): Boolean {
        return useDynamicColor
    }

    override suspend fun setDarkThemeConfig(config: DarkThemeConfig) {
        this.darkThemeConfig = config
    }

    override fun getDarkThemeConfig(): DarkThemeConfig {
        return darkThemeConfig
    }

    override suspend fun setGeneralSettings(generalSettings: GeneralSettings) {
        this.generalSettings = generalSettings
    }

    override fun getGeneralSettings(): GeneralSettings {
        return generalSettings
    }

    override suspend fun setPrivacySettings(privacySettings: PrivacySettings) {
        this.privacySettings = privacySettings
    }

    override fun getPrivacySettings(): PrivacySettings {
        return privacySettings
    }
}