package com.thejohnsondev.model

sealed class DatabaseResponse {
    object ResponseSuccess: DatabaseResponse()
    data class ResponseFailure(val exception: Exception?): DatabaseResponse()
}
