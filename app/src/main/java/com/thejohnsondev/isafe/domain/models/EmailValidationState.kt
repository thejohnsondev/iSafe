package com.thejohnsondev.isafe.domain.models

sealed class EmailValidationState {
    object EmailCorrectState : EmailValidationState()
    class EmailIncorrectState(val reasonResId: Int) : EmailValidationState()
}
