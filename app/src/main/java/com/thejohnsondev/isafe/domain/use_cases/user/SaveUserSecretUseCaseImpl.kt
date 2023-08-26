package com.thejohnsondev.isafe.domain.use_cases.user

import com.thejohnsondev.isafe.data.local_data_source.DataStore
import com.thejohnsondev.isafe.domain.models.DatabaseResponse
import com.thejohnsondev.isafe.domain.repositories.RemoteDbRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveUserSecretUseCaseImpl @Inject constructor(
    private val remoteDbRepository: RemoteDbRepository
): SaveUserSecretUseCase {
    override suspend fun invoke(userId: String, userSecret: String): Flow<DatabaseResponse> {
        return remoteDbRepository.updateUserSecret(userId, userSecret)
    }
}