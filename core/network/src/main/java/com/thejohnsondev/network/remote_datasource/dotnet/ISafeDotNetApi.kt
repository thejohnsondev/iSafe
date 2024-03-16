package com.thejohnsondev.network.remote_datasource.dotnet

import arrow.core.Either
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.model.auth.AuthBody
import com.thejohnsondev.model.auth.AuthResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ISafeDotNetApi {

    @POST(SIGN_UP)
    suspend fun signUp(
        @Body authBody: AuthBody
    ): Either<ApiError, AuthResponse>

    @POST(LOGIN)
    suspend fun login(
        @Body authBody: AuthBody
    ): Either<ApiError, AuthResponse>

    @GET(GET_PASSWORDS)
    suspend fun getPasswords(): Either<ApiError, List<PasswordModel>>

    @POST(ADD_PASSWORD)
    suspend fun addPassword(
        @Body passwordModel: PasswordModel
    ): Either<ApiError, PasswordModel>

    @PUT("$UPDATE_PASSWORD/{id}")
    suspend fun updatePassword(
        @Path("id") id: String,
        @Body passwordModel: PasswordModel
    ): Either<ApiError, Unit>

    @DELETE("$DELETE_PASSWORD/{id}")
    suspend fun deletePassword(
        @Path("id") id: String
    ): Either<ApiError, Unit>

    @DELETE(DELETE_ACCOUNT)
    suspend fun deleteAccount(): Either<ApiError, Unit>

    @GET(GET_NOTES)
    suspend fun getNotes(): Either<ApiError, List<NoteModel>>

    @POST(CREATE_NOTE)
    suspend fun createNote(
        @Body noteModel: NoteModel
    ): Either<ApiError, NoteModel>

    @PUT("$UPDATE_NOTE/{id}")
    suspend fun updateNote(
        @Path("id") id: String,
        @Body noteModel: NoteModel
    ): Either<ApiError, Unit>

    @DELETE("$DELETE_NOTE/{id}")
    suspend fun deleteNote(
        @Path("id") id: String
    ): Either<ApiError, Unit>

}