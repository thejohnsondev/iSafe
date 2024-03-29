package com.thejohnsondev.domain

import javax.inject.Inject

data class AuthUseCases @Inject constructor(
    val signUp: SignUpUseCase,
    val signIn: SignInUseCase,
    val validateEmail: EmailValidateUseCase,
    val validatePassword: PasswordValidationUseCase,
    val createUser: CreateUserUseCase,
    val getUserData: GetRemoteUserDataUseCase,
    val generateUserKey: GenerateUserKeyUseCase,
    val saveUserKey: SaveUserKeyUseCase,
    val saveUserToken: SaveUserTokenUseCase
)