package com.thejohnsondev.domain

import com.thejohnsondev.data.UserRepository
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.UserModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : CreateUserUseCase {
    override suspend fun invoke(userModel: UserModel): Flow<DatabaseResponse> {
        return userRepository.createUser(userModel)
    }
}