package com.thejohnsondev.domain

import javax.inject.Inject

data class AddNoteUseCases @Inject constructor(
    val createNote: CreateNoteUseCase
)