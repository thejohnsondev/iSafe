package com.thejohnsondev.isafe.domain.use_cases.notes

import com.thejohnsondev.isafe.domain.models.DatabaseResponse
import com.thejohnsondev.isafe.domain.models.NoteModel
import com.thejohnsondev.isafe.domain.repositories.NotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateNoteUseCaseImpl @Inject constructor(
    private val notesRepository: NotesRepository
): UpdateNoteUseCase {
    override suspend fun invoke(userId: String, note: NoteModel): Flow<DatabaseResponse> {
        return notesRepository.updateNote(userId, note)
    }
}