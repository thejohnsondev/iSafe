package com.thejohnsondev.isafe.domain.use_cases.user

import com.thejohnsondev.isafe.domain.models.UserModel

interface GetLocalUserDataUseCase {
    suspend operator fun invoke(): UserModel
}