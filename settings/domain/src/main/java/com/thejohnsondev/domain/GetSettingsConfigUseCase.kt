package com.thejohnsondev.domain

import com.thejohnsondev.data.SettingsRepository
import com.thejohnsondev.model.settings.SettingsConfig
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSettingsConfigUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<SettingsConfig> = settingsRepository.settingsConfig

}