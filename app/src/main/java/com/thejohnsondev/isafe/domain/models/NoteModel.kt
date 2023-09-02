package com.thejohnsondev.isafe.domain.models

data class NoteModel(
    val id: Int,
    val title: String,
    val description: String,
    val timeStamp: Long,
    val category: String
)
