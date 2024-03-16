package com.thejohnsondev.domain

import arrow.core.Either
import com.thejohnsondev.data.NotesRepository
import com.thejohnsondev.model.ApiError
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(noteId: String): Flow<Either<ApiError, Unit>> {
        return notesRepository.deleteNote(noteId)
    }
}