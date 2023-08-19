package com.thejohnsondev.isafe.domain.use_cases.user

import com.thejohnsondev.isafe.domain.models.DatabaseResponse
import kotlinx.coroutines.flow.Flow

interface SaveUserSecretUseCase {
    suspend operator fun invoke(userId: String, userSecret: String): Flow<DatabaseResponse>
}