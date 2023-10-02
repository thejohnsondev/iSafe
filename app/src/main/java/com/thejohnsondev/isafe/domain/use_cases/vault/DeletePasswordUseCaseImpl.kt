package com.thejohnsondev.isafe.domain.use_cases.vault

import com.thejohnsondev.isafe.domain.models.DatabaseResponse
import com.thejohnsondev.isafe.domain.repositories.PasswordsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeletePasswordUseCaseImpl @Inject constructor(
    private val passwordsRepository: PasswordsRepository
): DeletePasswordUseCase {
    override suspend fun invoke(userId: String, passwordId: String): Flow<DatabaseResponse> {
        return passwordsRepository.deletePassword(userId, passwordId)
    }
}