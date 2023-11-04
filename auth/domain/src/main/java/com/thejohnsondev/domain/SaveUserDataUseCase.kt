package com.thejohnsondev.domain

import com.thejohnsondev.model.UserModel

interface SaveUserDataUseCase {
    suspend operator fun invoke(userModel: UserModel, isFromLogin: Boolean)
}