package com.thejohnsondev.domain

import com.thejohnsondev.common.isPasswordValid
import com.thejohnsondev.model.PasswordValidationState

class PasswordValidationUseCaseImpl : PasswordValidationUseCase {

    override suspend fun invoke(password: String): PasswordValidationState {
        return password.isPasswordValid()
    }

}