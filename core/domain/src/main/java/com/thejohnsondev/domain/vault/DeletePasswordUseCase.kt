package com.thejohnsondev.domain.vault

import com.thejohnsondev.model.DatabaseResponse
import kotlinx.coroutines.flow.Flow

interface DeletePasswordUseCase {
    suspend operator fun invoke(userId: String, passwordId: String): Flow<DatabaseResponse>
}