package com.thejohnsondev.domain

import arrow.core.Either
import com.thejohnsondev.data.PasswordsRepository
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.HttpError
import com.thejohnsondev.model.PasswordModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class CreatePasswordUseCase @Inject constructor(
    private val passwordsRepository: PasswordsRepository
) {
    operator fun invoke(password: PasswordModel): Flow<Either<ApiError, PasswordModel>> {
        if (password.organization.isEmpty() && password.title.isEmpty() && password.password.isEmpty()) {
            return flowOf(Either.Left(HttpError(400, "Empty password")))
        }
        return passwordsRepository.createPassword(password)
    }
}