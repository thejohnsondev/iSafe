package com.thejohnsondev.isafe.domain.use_cases.auth

import com.thejohnsondev.isafe.domain.models.EmailValidationState


interface EmailValidateUseCase {

    suspend operator fun invoke(email: String): EmailValidationState

}