package com.thejohnsondev.isafe.domain.use_cases.auth

import com.thejohnsondev.isafe.domain.models.EmailValidationState
import com.thejohnsondev.isafe.utils.isEmailValid

class EmailValidationUseCaseImpl : EmailValidateUseCase {

    override suspend fun invoke(email: String): EmailValidationState {
        return email.isEmailValid()
    }

}