package com.thejohnsondev.domain

import com.thejohnsondev.data.PasswordsRepository
import com.thejohnsondev.model.DatabaseResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeletePasswordUseCaseImpl @Inject constructor(
    private val passwordsRepository: PasswordsRepository
) : DeletePasswordUseCase {
    override suspend fun invoke(userId: String, passwordId: String): Flow<DatabaseResponse> {
        return passwordsRepository.deletePassword(userId, passwordId)
    }
}