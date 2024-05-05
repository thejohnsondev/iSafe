package com.thejohnsondev.data

import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.model.settings.DarkThemeConfig
import com.thejohnsondev.model.settings.GeneralSettings
import com.thejohnsondev.model.settings.PrivacySettings
import com.thejohnsondev.model.settings.SettingsConfig
import com.thejohnsondev.model.settings.ThemeBrand
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore,
): SettingsRepository {

    private val _settingsConfig = MutableStateFlow(SettingsConfig(
        customTheme = dataStore.getCustomTheme(),
        useDynamicColor = dataStore.getUseDynamicColor(),
        darkThemeConfig = dataStore.getDarkThemeConfig(),
        generalSettings = dataStore.getGeneralSettings(),
        privacySettings = dataStore.getPrivacySettings()
    ))
    override val settingsConfig: Flow<SettingsConfig> = _settingsConfig

    override suspend fun updateCustomTheme(theme: ThemeBrand) {
        _settingsConfig.value = _settingsConfig.value.copy(customTheme = theme)
        dataStore.setCustomTheme(theme)
    }

    override suspend fun updateUseDynamicColor(useDynamicColor: Boolean) {
        _settingsConfig.value = _settingsConfig.value.copy(useDynamicColor = useDynamicColor)
        dataStore.setUseDynamicColor(useDynamicColor)
    }

    override suspend fun updateDarkThemeConfig(config: DarkThemeConfig) {
        _settingsConfig.value = _settingsConfig.value.copy(darkThemeConfig = config)
        dataStore.setDarkThemeConfig(config)
    }

    override suspend fun updateGeneralSettings(generalSettings: GeneralSettings) {
        _settingsConfig.value = _settingsConfig.value.copy(generalSettings = generalSettings)
        dataStore.setGeneralSettings(generalSettings)
    }

    override suspend fun updatePrivacySettings(privacySettings: PrivacySettings) {
        _settingsConfig.value = _settingsConfig.value.copy(privacySettings = privacySettings)
        dataStore.setPrivacySettings(privacySettings)
    }

}