package com.thejohnsondev.domain

import com.thejohnsondev.common.navigation.Screens
import com.thejohnsondev.data.AuthRepository
import com.thejohnsondev.data.SettingsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetFirstScreenRouteUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): String {
        val isUseBiometrics = settingsRepository
            .settingsConfig
            .first()
            .privacySettings
            .isUnlockWithBiometricEnabled

        return if (isUserLoggedIn()) {
            if (isUseBiometrics) {
                Screens.BiometricScreen.name
            } else {
                Screens.HomeScreen.name
            }
        } else {
            Screens.SignUpScreen.name
        }
    }

    private fun isUserLoggedIn(): Boolean = authRepository.isUserLoggedIn()
}