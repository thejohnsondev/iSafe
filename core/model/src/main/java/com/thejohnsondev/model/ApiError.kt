package com.thejohnsondev.model

sealed class ApiError

data class HttpError(
    val code: Int,
    val message: String,
) : ApiError()

data class NetworkError(val throwable: Throwable) : ApiError()

data class UnknownApiError(val throwable: Throwable) : ApiError()