package com.thejohnsondev.domain

import arrow.core.Either
import com.thejohnsondev.data.AuthRepository
import com.thejohnsondev.model.ApiError
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<Either<ApiError, Unit>> {
        return authRepository.deleteAccount()
    }
}