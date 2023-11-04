package com.thejohnsondev.data

import com.thejohnsondev.model.AuthResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun signUp(email: String, password: String): Flow<AuthResponse>
    suspend fun singIn(email: String, password: String): Flow<AuthResponse>
    suspend fun signOut()
    suspend fun getCurrentUserName(): Flow<String>
    suspend fun getCurrentUserId(): Flow<String>
    fun isUserLoggedIn(): Boolean

}