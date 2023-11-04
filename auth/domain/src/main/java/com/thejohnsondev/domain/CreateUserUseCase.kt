package com.thejohnsondev.domain

import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.UserModel
import kotlinx.coroutines.flow.Flow

interface CreateUserUseCase {
    suspend operator fun invoke(userModel: UserModel): Flow<DatabaseResponse>
}