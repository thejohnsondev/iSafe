package com.thejohnsondev.isafe.domain.models

sealed class UserNotesResponse {
    data class ResponseSuccess(val notes: List<NoteModel>) : UserNotesResponse()
    data class ResponseFailure(val exception: Exception?) : UserNotesResponse()
}
