package com.thejohnsondev.domain

import com.thejohnsondev.model.DatabaseResponse
import kotlinx.coroutines.flow.Flow

interface DeleteNoteUseCase {
    suspend operator fun invoke(userId: String, noteId: Int): Flow<DatabaseResponse>
}