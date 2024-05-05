package com.thejohnsondev.data

import com.thejohnsondev.model.settings.DarkThemeConfig
import com.thejohnsondev.model.settings.GeneralSettings
import com.thejohnsondev.model.settings.SettingsConfig
import com.thejohnsondev.model.settings.ThemeBrand
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    val settingsConfig: Flow<SettingsConfig>
    suspend fun setCustomTheme(theme: ThemeBrand)
    suspend fun setUseDynamicColor(useDynamicColor: Boolean)
    suspend fun setDarkThemeConfig(config: DarkThemeConfig)
    suspend fun setGeneralSettings(generalSettings: GeneralSettings)

}