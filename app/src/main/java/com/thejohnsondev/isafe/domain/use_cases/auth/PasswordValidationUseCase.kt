package com.thejohnsondev.isafe.domain.use_cases.auth

import com.thejohnsondev.isafe.domain.models.PasswordValidationState


interface PasswordValidationUseCase {

    suspend operator fun invoke(password: String): PasswordValidationState

}