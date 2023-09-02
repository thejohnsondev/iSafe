package com.thejohnsondev.isafe.domain.repositories

import com.thejohnsondev.isafe.domain.models.DatabaseResponse
import com.thejohnsondev.isafe.domain.models.NoteModel
import com.thejohnsondev.isafe.domain.models.UserNotesResponse
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getUserNotes(userId: String): Flow<UserNotesResponse>
    fun createNote(userId: String, note: NoteModel): Flow<DatabaseResponse>
    fun updateNote(userId: String, note: NoteModel): Flow<DatabaseResponse>
    fun deleteNote(userId: String, noteId: Int): Flow<DatabaseResponse>
}