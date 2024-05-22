package com.thejohnsondev.data

import com.thejohnsondev.model.settings.DarkThemeConfig
import com.thejohnsondev.model.settings.GeneralSettings
import com.thejohnsondev.model.settings.PrivacySettings
import com.thejohnsondev.model.settings.SettingsConfig
import com.thejohnsondev.model.settings.ThemeBrand
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeSettingsRepository: SettingsRepository {

    private val _settingsConfig = MutableStateFlow(SettingsConfig(
        customTheme = ThemeBrand.ANDROID,
        useDynamicColor = false,
        darkThemeConfig = DarkThemeConfig.DARK,
        generalSettings = GeneralSettings(),
        privacySettings = PrivacySettings()
    ))
    override val settingsConfig: Flow<SettingsConfig>
        get() = _settingsConfig

    override suspend fun updateCustomTheme(theme: ThemeBrand) {
        _settingsConfig.value = _settingsConfig.value.copy(customTheme = theme)
    }

    override suspend fun updateUseDynamicColor(useDynamicColor: Boolean) {
        _settingsConfig.value = _settingsConfig.value.copy(useDynamicColor = useDynamicColor)
    }

    override suspend fun updateDarkThemeConfig(config: DarkThemeConfig) {
        _settingsConfig.value = _settingsConfig.value.copy(darkThemeConfig = config)
    }

    override suspend fun updateGeneralSettings(generalSettings: GeneralSettings) {
        _settingsConfig.value = _settingsConfig.value.copy(generalSettings = generalSettings)
    }

    override suspend fun updatePrivacySettings(privacySettings: PrivacySettings) {
        _settingsConfig.value = _settingsConfig.value.copy(privacySettings = privacySettings)
    }

}