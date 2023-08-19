package com.thejohnsondev.isafe.domain.use_cases.user

import com.thejohnsondev.isafe.domain.models.UserDataResponse
import com.thejohnsondev.isafe.domain.repositories.RemoteDbRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRemoteUserDataUseCaseImpl @Inject constructor(
    private val remoteDbRepository: RemoteDbRepository
): GetRemoteUserDataUseCase {
    override suspend fun invoke(userId: String): Flow<UserDataResponse> {
        return remoteDbRepository.getUserData(userId)
    }
}