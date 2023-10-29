package com.thejohnsondev.domain

import com.thejohnsondev.common.isEmailValid
import com.thejohnsondev.model.EmailValidationState

class EmailValidationUseCaseImpl : EmailValidateUseCase {

    override suspend fun invoke(email: String): EmailValidationState {
        return email.isEmailValid()
    }

}