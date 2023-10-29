package com.thejohnsondev.domain

import com.thejohnsondev.data.UserRepository
import com.thejohnsondev.model.DatabaseResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveUserSecretUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : SaveUserSecretUseCase {
    override suspend fun invoke(userId: String, userSecret: String): Flow<DatabaseResponse> {
        return userRepository.updateUserSecret(userId, userSecret)
    }
}