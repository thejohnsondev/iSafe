package com.thejohnsondev.domain

import com.thejohnsondev.common.isEmailValid
import com.thejohnsondev.model.EmailValidationState
import javax.inject.Inject

class EmailValidateUseCase @Inject constructor(){

    operator fun invoke(email: String): EmailValidationState {
        return email.isEmailValid()
    }

}