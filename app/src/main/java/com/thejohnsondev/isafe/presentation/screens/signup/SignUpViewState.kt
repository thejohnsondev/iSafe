package com.thejohnsondev.isafe.presentation.screens.signup

import com.thejohnsondev.isafe.domain.models.EmailValidationState
import com.thejohnsondev.isafe.domain.models.LoadingState
import com.thejohnsondev.isafe.domain.models.PasswordValidationState

data class SignUpViewState(
    val isSignUpSuccess: Boolean? = null,
    val loadingState: LoadingState = LoadingState.Loaded,
    val emailValidationState: EmailValidationState? = null,
    val passwordValidationState: PasswordValidationState? = null,
    val signUpReady: Boolean = false
)
