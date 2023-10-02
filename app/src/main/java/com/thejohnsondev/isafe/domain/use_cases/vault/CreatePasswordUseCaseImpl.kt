package com.thejohnsondev.isafe.domain.use_cases.vault

import com.thejohnsondev.isafe.domain.models.DatabaseResponse
import com.thejohnsondev.isafe.domain.models.PasswordModel
import com.thejohnsondev.isafe.domain.repositories.PasswordsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreatePasswordUseCaseImpl @Inject constructor(
    private val passwordsRepository: PasswordsRepository
): CreatePasswordUseCase {
    override suspend fun invoke(userId: String, password: PasswordModel): Flow<DatabaseResponse> {
        return passwordsRepository.createPassword(userId, password)
    }
}