package com.thejohnsondev.domain

import arrow.core.Either
import com.thejohnsondev.data.NotesRepository
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.NoteModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNotesUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(): Flow<Either<ApiError, List<NoteModel>>> {
        return notesRepository.getNotes()
    }
}