package com.thejohnsondev.domain

import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.NoteModel
import kotlinx.coroutines.flow.Flow

interface UpdateNoteUseCase {
    suspend operator fun invoke(userId: String, note: NoteModel): Flow<DatabaseResponse>
}