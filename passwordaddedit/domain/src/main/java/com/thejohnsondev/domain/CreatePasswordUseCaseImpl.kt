package com.thejohnsondev.domain

import com.thejohnsondev.data.PasswordsRepository
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.PasswordModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class CreatePasswordUseCaseImpl @Inject constructor(
    private val passwordsRepository: PasswordsRepository
) : CreatePasswordUseCase {
    override suspend fun invoke(userId: String, password: PasswordModel): Flow<DatabaseResponse> {
        if (password.organization.isEmpty() && password.title.isEmpty() && password.password.isEmpty()) {
            return flowOf(DatabaseResponse.ResponseFailure(Exception("Your password is empty")))
        }
        return passwordsRepository.createPassword(userId, password)
    }
}