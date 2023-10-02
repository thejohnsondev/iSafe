package com.thejohnsondev.isafe.domain.use_cases.vault

import com.thejohnsondev.isafe.domain.models.DatabaseResponse
import kotlinx.coroutines.flow.Flow

interface DeletePasswordUseCase {
    suspend operator fun invoke(userId: String, passwordId: String): Flow<DatabaseResponse>
}