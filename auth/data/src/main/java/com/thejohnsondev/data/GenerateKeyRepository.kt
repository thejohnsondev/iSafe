package com.thejohnsondev.data

import com.thejohnsondev.model.KeyGenerateResult
import kotlinx.coroutines.flow.Flow

interface GenerateKeyRepository {
    fun generateKey(password: String): Flow<KeyGenerateResult>
}