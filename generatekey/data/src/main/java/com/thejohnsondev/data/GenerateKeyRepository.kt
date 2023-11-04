package com.thejohnsondev.data

import android.net.Uri
import com.thejohnsondev.model.KeyGenerateResult
import kotlinx.coroutines.flow.Flow

interface GenerateKeyRepository {
    fun generateKey(fileUri: Uri?): Flow<KeyGenerateResult>
}