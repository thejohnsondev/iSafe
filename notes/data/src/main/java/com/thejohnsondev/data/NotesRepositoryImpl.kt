package com.thejohnsondev.data

import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.model.UserNotesResponse
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class NotesRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : NotesRepository {

    override fun getUserNotes(userId: String): Flow<UserNotesResponse> =
        remoteDataSource.getUserNotes(userId)

    override fun createNote(userId: String, note: NoteModel): Flow<DatabaseResponse> =
        remoteDataSource.createNote(userId, note)

    override fun updateNote(userId: String, note: NoteModel): Flow<DatabaseResponse> =
        remoteDataSource.updateNote(userId, note)

    override fun deleteNote(userId: String, noteId: Int): Flow<DatabaseResponse> =
        remoteDataSource.deleteNote(userId, noteId)

}