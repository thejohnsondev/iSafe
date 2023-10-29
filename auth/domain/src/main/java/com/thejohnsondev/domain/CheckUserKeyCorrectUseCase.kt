package com.thejohnsondev.domain

import kotlinx.coroutines.flow.Flow

interface CheckUserKeyCorrectUseCase {
    suspend operator fun invoke(key: ByteArray): Flow<Boolean>
}