package com.thejohnsondev.data

import arrow.core.Either
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.NoteModel
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getNotes(): Flow<Either<ApiError, List<NoteModel>>>
    fun createNote(noteModel: NoteModel): Flow<Either<ApiError, NoteModel>>
    fun updateNote(
        noteModel: NoteModel
    ): Flow<Either<ApiError, Unit>>
    fun deleteNote(
        id: String
    ): Flow<Either<ApiError, Unit>>
}