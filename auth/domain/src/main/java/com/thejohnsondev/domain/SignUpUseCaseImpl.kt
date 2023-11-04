package com.thejohnsondev.domain

import com.thejohnsondev.data.AuthRepository
import com.thejohnsondev.model.AuthResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignUpUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : SignUpUseCase {

    override suspend operator fun invoke(
        email: String,
        password: String
    ): Flow<AuthResponse> {
        return authRepository.signUp(email, password)
    }

}