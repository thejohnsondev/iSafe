package com.thejohnsondev.model

data class NoteModel(
    val id: String? = null,
    val title: String,
    val description: String,
    val lastEdit: String
)
