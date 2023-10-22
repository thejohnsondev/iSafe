package com.thejohnsondev.model

sealed class UserPasswordsResponse {
    data class ResponseSuccess(val passwords: List<PasswordModel>) : UserPasswordsResponse()
    data class ResponseFailure(val exception: Exception?) : UserPasswordsResponse()
}