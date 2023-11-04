package com.thejohnsondev.model

sealed class EmailValidationState {
    object EmailCorrectState : EmailValidationState()
    class EmailIncorrectState(val reason: EmailIncorrectReason) : EmailValidationState()
}

enum class EmailIncorrectReason {
    INCORRECT
}
