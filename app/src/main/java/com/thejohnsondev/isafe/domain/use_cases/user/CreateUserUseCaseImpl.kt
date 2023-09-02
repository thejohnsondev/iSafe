package com.thejohnsondev.isafe.domain.use_cases.user

import com.thejohnsondev.isafe.domain.models.DatabaseResponse
import com.thejohnsondev.isafe.domain.models.UserModel
import com.thejohnsondev.isafe.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
): CreateUserUseCase {
    override suspend fun invoke(userModel: UserModel): Flow<DatabaseResponse> {
        return userRepository.createUser(userModel)
    }
}