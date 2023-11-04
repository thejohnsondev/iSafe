package com.thejohnsondev.domain

import com.thejohnsondev.model.UserNotesResponse
import kotlinx.coroutines.flow.Flow

interface GetAllNotesUseCase {
    suspend operator fun invoke(userId: String): Flow<UserNotesResponse>
}