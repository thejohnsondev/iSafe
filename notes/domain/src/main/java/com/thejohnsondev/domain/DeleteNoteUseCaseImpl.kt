package com.thejohnsondev.domain

import com.thejohnsondev.data.NotesRepository
import com.thejohnsondev.model.DatabaseResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteNoteUseCaseImpl @Inject constructor(
    private val notesRepository: NotesRepository
) : DeleteNoteUseCase {
    override suspend fun invoke(userId: String, noteId: Int): Flow<DatabaseResponse> {
        return notesRepository.deleteNote(userId, noteId)
    }
}