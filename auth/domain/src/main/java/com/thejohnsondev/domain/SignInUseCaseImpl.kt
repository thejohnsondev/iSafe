package com.thejohnsondev.domain

import com.thejohnsondev.data.AuthRepository
import com.thejohnsondev.model.AuthResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : SignInUseCase {

    override suspend operator fun invoke(
        email: String,
        password: String
    ): Flow<AuthResponse> {
        return authRepository.singIn(email, password)
    }

}