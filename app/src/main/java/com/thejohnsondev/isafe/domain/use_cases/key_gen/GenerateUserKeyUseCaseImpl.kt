package com.thejohnsondev.isafe.domain.use_cases.key_gen

import android.net.Uri
import com.thejohnsondev.isafe.domain.models.KeyGenerateResult
import com.thejohnsondev.isafe.domain.repositories.GenerateKeyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GenerateUserKeyUseCaseImpl @Inject constructor(
    private val generateKeyRepository: GenerateKeyRepository
) : GenerateUserKeyUseCase {
    override suspend fun invoke(fileUri: Uri?): Flow<KeyGenerateResult> {
        return generateKeyRepository.generateKey(fileUri)
    }
}