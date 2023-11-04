package com.thejohnsondev.domain

import com.thejohnsondev.data.UserRepository
import com.thejohnsondev.model.UserDataResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRemoteUserDataUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : GetRemoteUserDataUseCase {
    override suspend fun invoke(userId: String): Flow<UserDataResponse> {
        return userRepository.getUserData(userId)
    }
}