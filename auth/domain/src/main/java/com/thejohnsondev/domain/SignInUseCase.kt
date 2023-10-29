package com.thejohnsondev.domain

import com.thejohnsondev.model.AuthResponse
import kotlinx.coroutines.flow.Flow

interface SignInUseCase {

    suspend operator fun invoke(email: String, password: String): Flow<AuthResponse>

}