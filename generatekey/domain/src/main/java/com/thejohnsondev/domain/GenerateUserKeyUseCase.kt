package com.thejohnsondev.domain

import android.net.Uri
import com.thejohnsondev.model.KeyGenerateResult
import kotlinx.coroutines.flow.Flow

interface GenerateUserKeyUseCase {
    suspend operator fun invoke(fileUri: Uri?): Flow<KeyGenerateResult>
}