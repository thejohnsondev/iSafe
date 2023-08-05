package com.thejohnsondev.isafe.domain.use_cases.auth

import com.thejohnsondev.isafe.domain.models.PasswordValidationState
import com.thejohnsondev.isafe.utils.isPasswordValid

class PasswordValidationUseCaseImpl : PasswordValidationUseCase {

    override suspend fun invoke(password: String): PasswordValidationState {
        return password.isPasswordValid()
    }

}