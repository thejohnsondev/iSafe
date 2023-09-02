package com.thejohnsondev.isafe.domain.use_cases.notes

import com.thejohnsondev.isafe.domain.models.DatabaseResponse
import kotlinx.coroutines.flow.Flow

interface DeleteNoteUseCase {
    suspend operator fun invoke(userId: String, noteId: Int): Flow<DatabaseResponse>
}