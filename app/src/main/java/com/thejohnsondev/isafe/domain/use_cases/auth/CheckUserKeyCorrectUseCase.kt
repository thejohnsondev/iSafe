package com.thejohnsondev.isafe.domain.use_cases.auth

import kotlinx.coroutines.flow.Flow

interface CheckUserKeyCorrectUseCase {
    suspend operator fun invoke(key: ByteArray): Flow<Boolean>
}