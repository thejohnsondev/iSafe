package com.thejohnsondev.isafe.domain.models

sealed class UserDataResponse {
    data class ResponseSuccess(val userModel: UserModel): UserDataResponse()
    data class ResponseFailure(val exception: Exception?): UserDataResponse()
}