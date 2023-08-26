package com.thejohnsondev.isafe.domain.use_cases.combined

import com.thejohnsondev.isafe.domain.use_cases.auth.LogoutUseCase
import javax.inject.Inject

data class HomeUseCases @Inject constructor(
    val logout: LogoutUseCase
)
