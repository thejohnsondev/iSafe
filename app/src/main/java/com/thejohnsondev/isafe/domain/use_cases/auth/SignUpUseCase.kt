package com.thejohnsondev.isafe.domain.use_cases.auth

import com.thejohnsondev.isafe.domain.models.AuthResponse
import kotlinx.coroutines.flow.Flow

interface SignUpUseCase {

    suspend operator fun invoke(email: String, password: String): Flow<AuthResponse>

}