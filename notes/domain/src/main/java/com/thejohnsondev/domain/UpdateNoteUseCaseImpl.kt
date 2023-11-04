package com.thejohnsondev.domain

import com.thejohnsondev.data.NotesRepository
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.NoteModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateNoteUseCaseImpl @Inject constructor(
    private val notesRepository: NotesRepository
) : UpdateNoteUseCase {
    override suspend fun invoke(userId: String, note: NoteModel): Flow<DatabaseResponse> {
        return notesRepository.updateNote(userId, note)
    }
}