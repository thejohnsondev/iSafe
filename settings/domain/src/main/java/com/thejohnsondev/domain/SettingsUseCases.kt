package com.thejohnsondev.domain

import javax.inject.Inject

data class SettingsUseCases @Inject constructor(
    val logout: LogoutUseCase
)
