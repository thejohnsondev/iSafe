package com.thejohnsondev.domain

import com.thejohnsondev.model.PasswordValidationState


interface PasswordValidationUseCase {

    suspend operator fun invoke(password: String): PasswordValidationState

}