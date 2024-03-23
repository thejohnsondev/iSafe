package com.thejohnsondev.domain

import com.thejohnsondev.common.isPasswordValid
import com.thejohnsondev.model.PasswordValidationState
import javax.inject.Inject

class PasswordValidationUseCase @Inject constructor() {

    operator fun invoke(password: String): PasswordValidationState {
        return password.isPasswordValid()
    }

}