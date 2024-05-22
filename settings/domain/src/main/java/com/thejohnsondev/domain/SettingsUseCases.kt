package com.thejohnsondev.domain

import javax.inject.Inject

data class SettingsUseCases @Inject constructor(
    val logout: LogoutUseCase,
    val deleteAccount: DeleteAccountUseCase,
    val getUserEmail: GetUserEmailUseCase,
    val getSettingsConfig: GetSettingsConfigFlowUseCase,
    val updateSetting: UpdateSettingsUseCase
)
