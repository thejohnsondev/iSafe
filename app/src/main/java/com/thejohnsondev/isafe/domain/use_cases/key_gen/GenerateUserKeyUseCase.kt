package com.thejohnsondev.isafe.domain.use_cases.key_gen

import android.net.Uri
import com.thejohnsondev.isafe.domain.models.KeyGenerateResult
import kotlinx.coroutines.flow.Flow

interface GenerateUserKeyUseCase {
    suspend operator fun invoke(fileUri: Uri?): Flow<KeyGenerateResult>
}