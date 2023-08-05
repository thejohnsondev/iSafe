package com.thejohnsondev.isafe.domain.use_cases.auth

import com.thejohnsondev.isafe.domain.models.AuthResponse
import com.thejohnsondev.isafe.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow

class SignInUseCaseImpl(
    private val authRepository: AuthRepository
) : SignInUseCase {

    override suspend operator fun invoke(
        email: String,
        password: String
    ): Flow<AuthResponse> {
        return authRepository.singIn(email, password)
    }

}