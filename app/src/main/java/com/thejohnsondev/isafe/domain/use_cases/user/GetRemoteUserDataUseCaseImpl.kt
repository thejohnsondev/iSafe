package com.thejohnsondev.isafe.domain.use_cases.user

import com.thejohnsondev.isafe.domain.models.UserDataResponse
import com.thejohnsondev.isafe.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRemoteUserDataUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
): GetRemoteUserDataUseCase {
    override suspend fun invoke(userId: String): Flow<UserDataResponse> {
        return userRepository.getUserData(userId)
    }
}