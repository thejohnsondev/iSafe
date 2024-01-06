package com.thejohnsondev.domain

import com.thejohnsondev.model.KeyGenerateResult
import kotlinx.coroutines.flow.Flow

interface GenerateUserKeyUseCase {
    suspend operator fun invoke(password: String): Flow<KeyGenerateResult>
}