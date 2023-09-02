package com.thejohnsondev.isafe.domain.use_cases.user

import com.thejohnsondev.isafe.domain.models.DatabaseResponse
import com.thejohnsondev.isafe.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveUserSecretUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
): SaveUserSecretUseCase {
    override suspend fun invoke(userId: String, userSecret: String): Flow<DatabaseResponse> {
        return userRepository.updateUserSecret(userId, userSecret)
    }
}