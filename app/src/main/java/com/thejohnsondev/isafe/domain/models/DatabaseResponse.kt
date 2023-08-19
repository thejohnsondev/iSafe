package com.thejohnsondev.isafe.domain.models

sealed class DatabaseResponse {
    object ResponseSuccess: DatabaseResponse()
    data class ResponseFailure(val exception: Exception?): DatabaseResponse()
}
