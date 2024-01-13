package com.thejohnsondev.domain

import arrow.core.Either
import com.thejohnsondev.data.PasswordsRepository
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.model.UserPasswordsResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPasswordsUseCase @Inject constructor(
    private val passwordsRepository: PasswordsRepository
) {
    operator fun invoke(userId: String): Flow<Either<ApiError, List<PasswordModel>>> {
        return passwordsRepository.getUserPasswords(userId)
    }
}