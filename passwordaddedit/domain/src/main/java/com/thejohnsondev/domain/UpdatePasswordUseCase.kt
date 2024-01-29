package com.thejohnsondev.domain

import arrow.core.Either
import com.thejohnsondev.data.PasswordsRepository
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.PasswordModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdatePasswordUseCase @Inject constructor(
    private val passwordsRepository: PasswordsRepository
) {
    operator fun invoke(userId: String, password: PasswordModel): Flow<Either<ApiError, Unit>> {
        return passwordsRepository.updatePassword(userId, password)
    }
}