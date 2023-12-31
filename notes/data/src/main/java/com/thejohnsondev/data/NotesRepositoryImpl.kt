package com.thejohnsondev.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thejohnsondev.common.DEFAULT_ID
import com.thejohnsondev.common.NOTES_DB_REF
import com.thejohnsondev.common.PARAM_CATEGORY
import com.thejohnsondev.common.PARAM_DESCRIPTION
import com.thejohnsondev.common.PARAM_ID
import com.thejohnsondev.common.PARAM_TITLE
import com.thejohnsondev.common.USERS_DB_REF
import com.thejohnsondev.common.awaitChannelFlow
import com.thejohnsondev.common.decrypt
import com.thejohnsondev.common.encrypt
import com.thejohnsondev.common.sendOrNothing
import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.model.UserNotesResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NotesRepositoryImpl(
    private val firebaseDatabase: FirebaseDatabase,
    private val coroutineScope: CoroutineScope,
    private val dataStore: DataStore
) : NotesRepository {

    private suspend fun getKey(): ByteArray {
        return dataStore.getUserKey()
    }

    override fun getUserNotes(userId: String): Flow<UserNotesResponse> = awaitChannelFlow {
        firebaseDatabase.getReference(USERS_DB_REF)
            .child(userId)
            .child(NOTES_DB_REF)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    coroutineScope.launch {
                        val noteMap = snapshot.value as HashMap<String, HashMap<String, String?>?>?
                        val notesList = mutableListOf<NoteModel>()
                        if (noteMap == null) {
                            sendOrNothing(UserNotesResponse.ResponseSuccess(notesList))
                            close()
                        }
                        noteMap?.values?.forEach {
                            it?.let {
                                notesList.add(
                                    NoteModel(
                                        id = it[PARAM_ID] ?: DEFAULT_ID,
                                        title = it[PARAM_TITLE]?.decrypt(getKey()).orEmpty(),
                                        description = it[PARAM_DESCRIPTION]?.decrypt(getKey())
                                            .orEmpty(),
                                        category = it[PARAM_CATEGORY]?.decrypt(getKey()).orEmpty()
                                    )
                                )
                            }
                        }
                        sendOrNothing(UserNotesResponse.ResponseSuccess(notesList))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    coroutineScope.launch {
                        sendOrNothing(UserNotesResponse.ResponseFailure(error.toException()))
                    }
                }
            })
    }

    override fun createNote(userId: String, note: NoteModel): Flow<DatabaseResponse> =
        awaitChannelFlow {
            if (note.title.isEmpty() && note.description.isEmpty()) {
                sendOrNothing(DatabaseResponse.ResponseFailure(Exception("Your note is empty")))
                close()
            }
            val encryptedNote = NoteModel(
                id = note.id,
                title = note.title.encrypt(getKey()),
                description = note.description.encrypt(getKey()),
                category = note.category.encrypt(getKey())
            )
            firebaseDatabase.getReference(USERS_DB_REF)
                .child(userId)
                .child(NOTES_DB_REF)
                .child(encryptedNote.id)
                .setValue(encryptedNote)
                .addOnSuccessListener {
                    coroutineScope.launch {
                        sendOrNothing(DatabaseResponse.ResponseSuccess)
                    }
                }
                .addOnFailureListener {
                    coroutineScope.launch {
                        sendOrNothing(DatabaseResponse.ResponseFailure(it))
                    }
                }
        }

    override fun updateNote(userId: String, note: NoteModel): Flow<DatabaseResponse> =
        awaitChannelFlow {
            val noteRef = firebaseDatabase.getReference(USERS_DB_REF)
                .child(userId)
                .child(NOTES_DB_REF)
                .child(note.id)

            val newNoteValues = mapOf(
                PARAM_ID to note.id,
                PARAM_TITLE to note.title.encrypt(getKey()),
                PARAM_DESCRIPTION to note.description.encrypt(getKey()),
                PARAM_CATEGORY to note.category.encrypt(getKey())
            )
            noteRef.updateChildren(newNoteValues)
                .addOnSuccessListener {
                    coroutineScope.launch {
                        sendOrNothing(DatabaseResponse.ResponseSuccess)
                    }
                }
                .addOnFailureListener {
                    coroutineScope.launch {
                        sendOrNothing(DatabaseResponse.ResponseFailure(it))
                    }
                }
        }

    override fun deleteNote(userId: String, noteId: Int): Flow<DatabaseResponse> =
        awaitChannelFlow {
            firebaseDatabase.getReference(USERS_DB_REF)
                .child(userId)
                .child(NOTES_DB_REF)
                .child(noteId.toString())
                .removeValue()
                .addOnSuccessListener {
                    coroutineScope.launch {
                        sendOrNothing(DatabaseResponse.ResponseSuccess)
                    }
                }
                .addOnFailureListener {
                    coroutineScope.launch {
                        sendOrNothing(DatabaseResponse.ResponseFailure(it))
                    }
                }
        }

}