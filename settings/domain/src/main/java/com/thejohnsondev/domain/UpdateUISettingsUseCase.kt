package com.thejohnsondev.domain

import com.thejohnsondev.data.SettingsRepository
import com.thejohnsondev.model.settings.DarkThemeConfig
import com.thejohnsondev.model.settings.ThemeBrand
import javax.inject.Inject

class UpdateUISettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(
        themeBrand: ThemeBrand? = null,
        useDynamicColor: Boolean? = null,
        darkThemeConfig: DarkThemeConfig? = null
    ) {
        themeBrand?.let { settingsRepository.setCustomTheme(it) }
        useDynamicColor?.let { settingsRepository.setUseDynamicColor(it) }
        darkThemeConfig?.let { settingsRepository.setDarkThemeConfig(it) }
    }

}