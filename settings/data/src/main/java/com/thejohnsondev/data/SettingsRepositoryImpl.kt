package com.thejohnsondev.data

import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.model.settings.DarkThemeConfig
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
        darkThemeConfig = dataStore.getDarkThemeConfig()
    ))
    override val settingsConfig: Flow<SettingsConfig> = _settingsConfig

    override suspend fun setCustomTheme(theme: ThemeBrand) {
        _settingsConfig.value = _settingsConfig.value.copy(customTheme = theme)
        dataStore.setCustomTheme(theme)
    }

    override suspend fun setUseDynamicColor(useDynamicColor: Boolean) {
        _settingsConfig.value = _settingsConfig.value.copy(useDynamicColor = useDynamicColor)
        dataStore.setUseDynamicColor(useDynamicColor)
    }

    override suspend fun setDarkThemeConfig(config: DarkThemeConfig) {
        _settingsConfig.value = _settingsConfig.value.copy(darkThemeConfig = config)
        dataStore.setDarkThemeConfig(config)
    }
}