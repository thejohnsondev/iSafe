package com.thejohnsondev.isafe.domain.use_cases.combined

import com.thejohnsondev.isafe.domain.use_cases.auth.EmailValidateUseCase
import com.thejohnsondev.isafe.domain.use_cases.auth.PasswordValidationUseCase
import com.thejohnsondev.isafe.domain.use_cases.auth.SignInUseCase
import com.thejohnsondev.isafe.domain.use_cases.auth.SignUpUseCase
import com.thejohnsondev.isafe.domain.use_cases.user.CreateUserUseCase
import com.thejohnsondev.isafe.domain.use_cases.user.GetUserDataUseCase
import com.thejohnsondev.isafe.domain.use_cases.user.SaveUserDataUseCase

data class AuthUseCases(
    val signUp: SignUpUseCase,
    val signIn: SignInUseCase,
    val validateEmail: EmailValidateUseCase,
    val validatePassword: PasswordValidationUseCase,
    val createUser: CreateUserUseCase,
    val saveUserData: SaveUserDataUseCase,
    val getUserData: GetUserDataUseCase
)