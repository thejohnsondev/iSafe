package com.thejohnsondev.domain

import arrow.core.Either
import com.thejohnsondev.data.PasswordsRepository
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.DatabaseResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeletePasswordUseCase @Inject constructor(
    private val passwordsRepository: PasswordsRepository
) {
    operator fun invoke(passwordId: String): Flow<Either<ApiError, Unit>> {
        return passwordsRepository.deletePassword(passwordId)
    }
}