package com.thejohnsondev.domain

import arrow.core.Either
import com.thejohnsondev.data.AuthRepository
import com.thejohnsondev.model.ApiError
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(
        oldPassword: String,
        newPassword: String
    ): Flow<Either<ApiError, Unit>> {
        return authRepository.changePassword(oldPassword, newPassword)
    }

}