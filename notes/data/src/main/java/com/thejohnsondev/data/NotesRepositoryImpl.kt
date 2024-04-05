package com.thejohnsondev.data

import arrow.core.Either
import com.thejohnsondev.database.local_datasource.LocalDataSource
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.network.di.DotNetRemoteDataSource
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class NotesRepositoryImpl(
    @DotNetRemoteDataSource private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : NotesRepository {
    override fun getNotes(): Flow<Either<ApiError, List<NoteModel>>> = remoteDataSource.getNotes()

    override fun createNote(noteModel: NoteModel): Flow<Either<ApiError, NoteModel>> =
        remoteDataSource.createNote(noteModel)

    override fun updateNote(noteModel: NoteModel): Flow<Either<ApiError, Unit>> =
        remoteDataSource.updateNote(
            noteModel
        )

    override fun deleteNote(id: String): Flow<Either<ApiError, Unit>> =
        remoteDataSource.deleteNote(id)


}