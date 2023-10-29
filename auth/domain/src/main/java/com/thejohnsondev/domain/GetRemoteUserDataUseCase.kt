package com.thejohnsondev.domain

import com.thejohnsondev.model.UserDataResponse
import kotlinx.coroutines.flow.Flow

interface GetRemoteUserDataUseCase {
    suspend operator fun invoke(userId: String): Flow<UserDataResponse>
}