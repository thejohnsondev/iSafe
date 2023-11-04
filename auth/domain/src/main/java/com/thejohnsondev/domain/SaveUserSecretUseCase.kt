package com.thejohnsondev.domain

import com.thejohnsondev.model.DatabaseResponse
import kotlinx.coroutines.flow.Flow

interface SaveUserSecretUseCase {
    suspend operator fun invoke(userId: String, userSecret: String): Flow<DatabaseResponse>
}