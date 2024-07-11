package com.thejohnsondev.data

import arrow.core.Either
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.auth.AuthResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun signUp(email: String, password: String): Flow<Either<ApiError, AuthResponse>>
    suspend fun singIn(email: String, password: String): Flow<Either<ApiError, AuthResponse>>
    suspend fun signOut()
    fun isUserLoggedIn(): Boolean
    suspend fun deleteAccount(): Flow<Either<ApiError, Unit>>
    suspend fun changePassword(oldPassword: String, newPassword: String): Flow<Either<ApiError, Unit>>

}