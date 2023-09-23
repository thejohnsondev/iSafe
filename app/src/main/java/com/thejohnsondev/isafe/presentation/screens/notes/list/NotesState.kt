package com.thejohnsondev.isafe.presentation.screens.notes.list

import com.thejohnsondev.isafe.domain.models.LoadingState
import com.thejohnsondev.isafe.domain.models.NoteModel

data class NotesState(
    val loadingState: LoadingState = LoadingState.Loaded,
    val notesList: List<NoteModel> = emptyList()
)