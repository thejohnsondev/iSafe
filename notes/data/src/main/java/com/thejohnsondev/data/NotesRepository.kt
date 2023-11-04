package com.thejohnsondev.data

import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.model.UserNotesResponse
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getUserNotes(userId: String): Flow<UserNotesResponse>
    fun createNote(userId: String, note: NoteModel): Flow<DatabaseResponse>
    fun updateNote(userId: String, note: NoteModel): Flow<DatabaseResponse>
    fun deleteNote(userId: String, noteId: Int): Flow<DatabaseResponse>
}