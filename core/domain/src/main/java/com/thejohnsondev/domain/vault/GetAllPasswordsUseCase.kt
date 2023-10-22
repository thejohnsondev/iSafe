package com.thejohnsondev.domain.vault

import com.thejohnsondev.model.UserPasswordsResponse
import kotlinx.coroutines.flow.Flow

interface GetAllPasswordsUseCase {
    suspend operator fun invoke(userId: String): Flow<UserPasswordsResponse>
}