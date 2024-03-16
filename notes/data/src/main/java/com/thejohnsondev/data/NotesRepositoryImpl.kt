package com.thejohnsondev.data

import arrow.core.Either
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class NotesRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : NotesRepository {
    override fun getNotes(): Flow<Either<ApiError, List<NoteModel>>> = remoteDataSource.getNotes()

    override fun createNote(noteModel: NoteModel): Flow<Either<ApiError, NoteModel>> =
        remoteDataSource.createNote(noteModel)

    override fun updateNote(id: String, noteModel: NoteModel): Flow<Either<ApiError, Unit>> =
        remoteDataSource.updateNote(
            id,
            noteModel
        )

    override fun deleteNote(id: String): Flow<Either<ApiError, Unit>> =
        remoteDataSource.deleteNote(id)


}