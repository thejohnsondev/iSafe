package com.thejohnsondev.isafe.presentation.screens.feat.notes.add_note

import com.thejohnsondev.isafe.domain.models.LoadingState

data class AddNoteState(
    val loadingState: LoadingState = LoadingState.Loaded,
    val titleState: String = "",
    val descriptionState: String = ""
)