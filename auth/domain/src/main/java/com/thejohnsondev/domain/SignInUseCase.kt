package com.thejohnsondev.domain

import arrow.core.Either
import com.thejohnsondev.data.AuthRepository
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.auth.AuthResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Flow<Either<ApiError, AuthResponse>> {
        return authRepository.singIn(email, password)
    }

}