package com.thejohnsondev.isafe.presentation.screens.signup

sealed class SignUpAction {
    class SignUpWithEmail(val email: String, val password: String): SignUpAction()
    class ValidateEmail(val email: String): SignUpAction()
    class ValidatePassword(val password: String): SignUpAction()
    class EnterName(val name: String): SignUpAction()
}