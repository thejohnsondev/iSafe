package com.thejohnsondev.isafe.domain.models

sealed class PasswordValidationState {
    object PasswordCorrectState : PasswordValidationState()
    class PasswordIncorrectState(val reasonResId: Int) : PasswordValidationState()
}
