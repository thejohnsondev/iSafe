package com.thejohnsondev.domain

import com.thejohnsondev.data.PasswordsRepository
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.PasswordModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreatePasswordUseCaseImpl @Inject constructor(
    private val passwordsRepository: PasswordsRepository
) : CreatePasswordUseCase {
    override suspend fun invoke(userId: String, password: PasswordModel): Flow<DatabaseResponse> {
        return passwordsRepository.createPassword(userId, password)
    }
}