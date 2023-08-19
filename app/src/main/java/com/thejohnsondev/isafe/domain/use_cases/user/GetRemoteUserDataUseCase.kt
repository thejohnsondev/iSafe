package com.thejohnsondev.isafe.domain.use_cases.user

import com.thejohnsondev.isafe.domain.models.UserDataResponse
import kotlinx.coroutines.flow.Flow

interface GetRemoteUserDataUseCase {
    suspend operator fun invoke(userId: String): Flow<UserDataResponse>
}