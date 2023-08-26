package com.thejohnsondev.isafe.domain.use_cases.combined

import com.thejohnsondev.isafe.domain.use_cases.auth.CheckUserKeyCorrectUseCase
import com.thejohnsondev.isafe.domain.use_cases.key_gen.GenerateUserKeyUseCase
import com.thejohnsondev.isafe.domain.use_cases.key_gen.SaveUserKeyUseCase
import com.thejohnsondev.isafe.domain.use_cases.user.GetLocalUserDataUseCase
import javax.inject.Inject

data class EnterEncryptionKeyUseCases @Inject constructor(
    val generateUserKey: GenerateUserKeyUseCase,
    val checkUserKeyCorrect: CheckUserKeyCorrectUseCase,
    val saveUserKey: SaveUserKeyUseCase,
)