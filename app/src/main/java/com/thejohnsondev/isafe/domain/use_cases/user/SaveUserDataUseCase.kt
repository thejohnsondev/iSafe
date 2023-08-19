package com.thejohnsondev.isafe.domain.use_cases.user

import com.thejohnsondev.isafe.domain.models.UserModel

interface SaveUserDataUseCase {
    suspend operator fun invoke(userModel: UserModel)
}