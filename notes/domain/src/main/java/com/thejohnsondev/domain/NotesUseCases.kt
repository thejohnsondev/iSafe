package com.thejohnsondev.domain

import javax.inject.Inject

data class NotesUseCases @Inject constructor(
    val createNote: CreateNoteUseCase,
    val deleteNote: DeleteNoteUseCase,
    val getAllNotes: GetAllNotesUseCase,
    val updateNote: UpdateNoteUseCase
)
