package com.thejohnsondev.isafe.domain.use_cases.combined

import com.thejohnsondev.isafe.domain.use_cases.notes.CreateNoteUseCase
import javax.inject.Inject

data class AddNoteUseCases @Inject constructor(
    val createNote: CreateNoteUseCase
)