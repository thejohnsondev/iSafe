package com.thejohnsondev.isafe.presentation.screens.auth.login

import com.thejohnsondev.isafe.domain.models.EmailValidationState
import com.thejohnsondev.isafe.domain.models.LoadingState
import com.thejohnsondev.isafe.domain.models.PasswordValidationState

data class LoginViewState(
    val isLoginSuccess: Boolean? = null,
    val loadingState: LoadingState = LoadingState.Loaded,
    val emailValidationState: EmailValidationState? = null,
    val passwordValidationState: PasswordValidationState? = null,
    val loginReady: Boolean = false
)