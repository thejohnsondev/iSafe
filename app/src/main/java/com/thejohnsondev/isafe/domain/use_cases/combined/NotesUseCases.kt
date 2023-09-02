package com.thejohnsondev.isafe.domain.use_cases.combined

import com.thejohnsondev.isafe.domain.use_cases.notes.CreateNoteUseCase
import com.thejohnsondev.isafe.domain.use_cases.notes.DeleteNoteUseCase
import com.thejohnsondev.isafe.domain.use_cases.notes.GetAllNotesUseCase
import com.thejohnsondev.isafe.domain.use_cases.notes.UpdateNoteUseCase
import javax.inject.Inject

data class NotesUseCases @Inject constructor(
    val createNote: CreateNoteUseCase,
    val deleteNote: DeleteNoteUseCase,
    val getAllNotes: GetAllNotesUseCase,
    val updateNote: UpdateNoteUseCase
)
