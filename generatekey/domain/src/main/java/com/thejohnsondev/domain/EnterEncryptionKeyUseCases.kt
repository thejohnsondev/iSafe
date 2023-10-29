package com.thejohnsondev.domain

import javax.inject.Inject

data class EnterEncryptionKeyUseCases @Inject constructor(
    val generateUserKey: GenerateUserKeyUseCase,
    val checkUserKeyCorrect: CheckUserKeyCorrectUseCase,
    val saveUserKey: SaveUserKeyUseCase,
    val logout: LogoutUseCase
)