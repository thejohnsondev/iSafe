package com.thejohnsondev.data

import arrow.core.Either
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.NoteModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeNotesRepository: NotesRepository {
    override fun getNotes(): Flow<Either<ApiError, List<NoteModel>>> = flowOf(Either.Right(
        listOf(
            NoteModel(
                "1",
                "Title 1",
                "Description 1",
                "2021-09-01T00:00:00Z"
            ),
            NoteModel(
                "2",
                "Title 2",
                "Description 2",
                "2021-09-02T00:00:00Z"
            )
        )
    ))
    override fun createNote(noteModel: NoteModel): Flow<Either<ApiError, NoteModel>> = flowOf(Either.Right(noteModel))
    override fun updateNote(
        noteModel: NoteModel
    ): Flow<Either<ApiError, Unit>> = flowOf(Either.Right(Unit))
    override fun deleteNote(
        id: String
    ): Flow<Either<ApiError, Unit>> = flowOf(Either.Right(Unit))
}