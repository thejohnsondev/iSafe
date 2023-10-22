package com.thejohnsondev.domain.vault

import com.thejohnsondev.data.vault.PasswordsRepository
import com.thejohnsondev.model.UserPasswordsResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPasswordsUseCaseImpl @Inject constructor(
    private val passwordsRepository: PasswordsRepository
) : GetAllPasswordsUseCase {
    override suspend fun invoke(userId: String): Flow<UserPasswordsResponse> {
        return passwordsRepository.getUserPasswords(userId)
    }
}