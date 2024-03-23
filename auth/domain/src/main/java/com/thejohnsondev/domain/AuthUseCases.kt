package com.thejohnsondev.domain

import javax.inject.Inject

data class AuthUseCases @Inject constructor(
    val signUp: SignUpUseCase,
    val signIn: SignInUseCase,
    val validateEmail: EmailValidateUseCase,
    val validatePassword: PasswordValidationUseCase,
    val generateUserKey: GenerateUserKeyUseCase,
    val saveUserKey: SaveUserKeyUseCase,
    val saveUserToken: SaveUserTokenUseCase
)