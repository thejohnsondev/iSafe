package com.thejohnsondev.isafe.domain.use_cases.auth

import kotlinx.coroutines.flow.Flow

interface LogoutUseCase {
    suspend operator fun invoke(): Flow<Boolean>
}