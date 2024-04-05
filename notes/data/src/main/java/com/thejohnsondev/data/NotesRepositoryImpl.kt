package com.thejohnsondev.data

import arrow.core.Either
import com.thejohnsondev.common.awaitChannelFlow
import com.thejohnsondev.common.sendOrNothing
import com.thejohnsondev.database.local_datasource.LocalDataSource
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.network.di.DotNetRemoteDataSource
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class NotesRepositoryImpl(
    @DotNetRemoteDataSource private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : NotesRepository {
    override fun getNotes(): Flow<Either<ApiError, List<NoteModel>>> = awaitChannelFlow {
        send(Either.Right(localDataSource.getNotes()))
        remoteDataSource.getNotes().first().fold(
            ifLeft = { sendOrNothing(Either.Left(it)) },
            ifRight = {
                localDataSource.updateNotes(it)
                sendOrNothing(Either.Right(it))
            }
        )
    }

    override fun createNote(noteModel: NoteModel): Flow<Either<ApiError, NoteModel>> = awaitChannelFlow {
        remoteDataSource.createNote(noteModel).first().fold(
            ifLeft = { sendOrNothing(Either.Left(it)) },
            ifRight = {
                localDataSource.createNote(it)
                sendOrNothing(Either.Right(it))
            }
        )
    }


    override fun updateNote(noteModel: NoteModel): Flow<Either<ApiError, Unit>> = awaitChannelFlow {
        remoteDataSource.updateNote(
            noteModel
        ).first().fold(
            ifLeft = { sendOrNothing(Either.Left(it)) },
            ifRight = {
                localDataSource.updateNote(noteModel)
                sendOrNothing(Either.Right(Unit))
            }
        )
    }


    override fun deleteNote(id: String): Flow<Either<ApiError, Unit>> = awaitChannelFlow {
        remoteDataSource.deleteNote(id).first().fold(
            ifLeft = { sendOrNothing(Either.Left(it)) },
            ifRight = {
                localDataSource.deleteNote(id)
                sendOrNothing(Either.Right(Unit))
            }
        )
    }


}