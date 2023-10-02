package com.thejohnsondev.isafe.domain.use_cases.vault

import com.thejohnsondev.isafe.domain.models.UserPasswordsResponse
import com.thejohnsondev.isafe.domain.repositories.PasswordsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPasswordsUseCaseImpl @Inject constructor(
    private val passwordsRepository: PasswordsRepository
): GetAllPasswordsUseCase {
    override suspend fun invoke(userId: String): Flow<UserPasswordsResponse> {
        return passwordsRepository.getUserPasswords(userId)
    }
}