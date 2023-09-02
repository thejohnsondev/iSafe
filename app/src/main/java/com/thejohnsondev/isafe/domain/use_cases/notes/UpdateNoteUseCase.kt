package com.thejohnsondev.isafe.domain.use_cases.notes

import com.thejohnsondev.isafe.domain.models.DatabaseResponse
import com.thejohnsondev.isafe.domain.models.NoteModel
import kotlinx.coroutines.flow.Flow

interface UpdateNoteUseCase {
    suspend operator fun invoke(userId: String, note: NoteModel): Flow<DatabaseResponse>
}