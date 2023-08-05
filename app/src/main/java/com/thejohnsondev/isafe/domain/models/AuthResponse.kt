package com.thejohnsondev.isafe.domain.models

import com.google.firebase.auth.AuthResult

sealed class AuthResponse {
    data class ResponseSuccess(val authResult: AuthResult): AuthResponse()
    data class ResponseFailure(val exception: Exception?): AuthResponse()
}
