package com.thejohnsondev.isafe.domain.use_cases.vault

import com.thejohnsondev.isafe.domain.models.UserPasswordsResponse
import kotlinx.coroutines.flow.Flow

interface GetAllPasswordsUseCase {
    suspend operator fun invoke(userId: String): Flow<UserPasswordsResponse>
}