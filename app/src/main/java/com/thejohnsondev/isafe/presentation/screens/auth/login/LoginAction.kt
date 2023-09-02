package com.thejohnsondev.isafe.presentation.screens.auth.login

sealed class LoginAction {
    class LoginWithEmail(val email: String, val password: String) : LoginAction()
    class ValidateEmail(val email: String) : LoginAction()
    class ValidatePassword(val password: String) : LoginAction()
}