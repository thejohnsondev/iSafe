package com.thejohnsondev.database.local_datasource

import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.model.PasswordModel

interface LocalDataSource {
    suspend fun getUserPasswords(): List<PasswordModel>
    suspend fun createPassword(passwordModel: PasswordModel): PasswordModel
    suspend fun updatePassword(passwordModel: PasswordModel)
    suspend fun updatePasswordsList(newPasswordList: List<PasswordModel>)
    suspend fun deletePassword(passwordId: String)
    suspend fun logout()
    suspend fun updatePasswords(passwords: List<PasswordModel>)

    suspend fun getNotes(): List<NoteModel>
    suspend fun createNote(noteModel: NoteModel): NoteModel
    suspend fun updateNote(noteModel: NoteModel)
    suspend fun deleteNote(noteId: String)
    suspend fun updateNotes(notes: List<NoteModel>)

}