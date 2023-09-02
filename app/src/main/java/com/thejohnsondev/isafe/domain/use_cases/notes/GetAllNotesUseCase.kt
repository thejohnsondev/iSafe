package com.thejohnsondev.isafe.domain.use_cases.notes

import com.thejohnsondev.isafe.domain.models.UserNotesResponse
import kotlinx.coroutines.flow.Flow

interface GetAllNotesUseCase {
    suspend operator fun invoke(userId: String): Flow<UserNotesResponse>
}