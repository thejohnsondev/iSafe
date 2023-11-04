package com.thejohnsondev.model


sealed class AuthResponse {
    data class ResponseSuccess(val userId: String) : AuthResponse()
    data class ResponseFailure(val exception: Exception?) : AuthResponse()
}
