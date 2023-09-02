package com.thejohnsondev.isafe.domain.use_cases.notes

import com.thejohnsondev.isafe.domain.models.DatabaseResponse
import com.thejohnsondev.isafe.domain.repositories.NotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteNoteUseCaseImpl @Inject constructor(
    private val notesRepository: NotesRepository
) : DeleteNoteUseCase {
    override suspend fun invoke(userId: String, noteId: Int): Flow<DatabaseResponse> {
        return notesRepository.deleteNote(userId, noteId)
    }
}