package com.thejohnsondev.domain

import javax.inject.Inject

data class CreateKeyUseCases @Inject constructor(
    val saveUserSecret: SaveUserSecretUseCase,
    val saveUserKey: SaveUserKeyUseCase,
    val generateUserKey: GenerateUserKeyUseCase,
    val getLocalUserData: GetLocalUserDataUseCase,
    val logout: LogoutUseCase
)