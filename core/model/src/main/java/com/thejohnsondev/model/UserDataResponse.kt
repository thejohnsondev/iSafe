package com.thejohnsondev.model

sealed class UserDataResponse {
    data class ResponseSuccess(val userModel: UserModel): UserDataResponse()
    data class ResponseFailure(val exception: Exception?): UserDataResponse()
}