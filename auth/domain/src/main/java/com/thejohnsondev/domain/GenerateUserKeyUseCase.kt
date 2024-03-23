package com.thejohnsondev.domain

import com.thejohnsondev.data.GenerateKeyRepository
import com.thejohnsondev.model.KeyGenerateResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GenerateUserKeyUseCase @Inject constructor(
    private val generateKeyRepository: GenerateKeyRepository
) {
    operator fun invoke(password: String): Flow<KeyGenerateResult> {
        return generateKeyRepository.generateKey(password)
    }
}