package com.thejohnsondev.domain

import com.thejohnsondev.model.UserModel

interface GetLocalUserDataUseCase {
    suspend operator fun invoke(): UserModel
}