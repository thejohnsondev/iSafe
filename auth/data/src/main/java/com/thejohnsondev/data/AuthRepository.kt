package com.thejohnsondev.data

import arrow.core.Either
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.auth.AuthResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun signUp(email: String, password: String): Flow<Either<ApiError, AuthResponse>>
    suspend fun singIn(email: String, password: String): Flow<Either<ApiError, AuthResponse>>
    suspend fun signOut()
    suspend fun getCurrentUserName(): Flow<String>
    suspend fun getCurrentUserId(): Flow<String>
    fun isUserLoggedIn(): Boolean

}