package com.thejohnsondev.database.local_datasource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.thejohnsondev.database.local_datasource.room.entity.NoteEntity

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: String)

    @Query("SELECT * FROM notes")
    suspend fun getAllNotes(): List<NoteEntity>

    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()

}