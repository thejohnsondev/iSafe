package com.thejohnsondev.isafe.domain.use_cases.combined

import com.thejohnsondev.isafe.domain.use_cases.key_gen.GenerateUserKeyUseCase
import com.thejohnsondev.isafe.domain.use_cases.key_gen.SaveUserKeyUseCase
import com.thejohnsondev.isafe.domain.use_cases.user.GetLocalUserDataUseCase
import com.thejohnsondev.isafe.domain.use_cases.user.SaveUserSecretUseCase
import javax.inject.Inject

data class CreateKeyUseCases @Inject constructor(
    val saveUserSecret: SaveUserSecretUseCase,
    val saveUserKey: SaveUserKeyUseCase,
    val generateUserKey: GenerateUserKeyUseCase,
    val getUserData: GetLocalUserDataUseCase
)