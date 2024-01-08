package com.thejohnsondev.network.remote_datasource.dotnet

import arrow.core.Either
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.auth.AuthBody
import com.thejohnsondev.model.auth.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ISafeDotNetApi {

    @POST(SIGN_UP)
    suspend fun signUp(
        @Body authBody: AuthBody
    ): Either<ApiError, AuthResponse>

    @POST(LOGIN)
    suspend fun login(
        @Body authBody: AuthBody
    ): Either<ApiError, AuthResponse>

}