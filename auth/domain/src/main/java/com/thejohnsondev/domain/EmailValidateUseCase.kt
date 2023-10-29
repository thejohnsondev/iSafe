package com.thejohnsondev.domain

import com.thejohnsondev.model.EmailValidationState


interface EmailValidateUseCase {

    suspend operator fun invoke(email: String): EmailValidationState

}