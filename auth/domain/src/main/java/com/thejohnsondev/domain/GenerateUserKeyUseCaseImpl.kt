package com.thejohnsondev.domain

import com.thejohnsondev.data.GenerateKeyRepository
import com.thejohnsondev.model.KeyGenerateResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GenerateUserKeyUseCaseImpl @Inject constructor(
    private val generateKeyRepository: GenerateKeyRepository
) : GenerateUserKeyUseCase {
    override suspend fun invoke(password: String): Flow<KeyGenerateResult> {
        return generateKeyRepository.generateKey(password)
    }
}