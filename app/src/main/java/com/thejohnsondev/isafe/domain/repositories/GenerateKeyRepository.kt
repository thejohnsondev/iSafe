package com.thejohnsondev.isafe.domain.repositories

import android.net.Uri
import com.thejohnsondev.isafe.domain.models.KeyGenerateResult
import kotlinx.coroutines.flow.Flow

interface GenerateKeyRepository {
    fun generateKey(fileUri: Uri?): Flow<KeyGenerateResult>
}