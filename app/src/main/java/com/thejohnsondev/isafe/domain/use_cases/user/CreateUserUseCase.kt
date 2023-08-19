package com.thejohnsondev.isafe.domain.use_cases.user

import com.thejohnsondev.isafe.domain.models.DatabaseResponse
import com.thejohnsondev.isafe.domain.models.UserModel
import kotlinx.coroutines.flow.Flow

interface CreateUserUseCase {
    suspend operator fun invoke(userModel: UserModel): Flow<DatabaseResponse>
}