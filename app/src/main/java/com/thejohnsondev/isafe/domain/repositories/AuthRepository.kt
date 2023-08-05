package com.thejohnsondev.isafe.domain.repositories

import com.thejohnsondev.isafe.domain.models.AuthResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun signUp(email: String, password: String): Flow<AuthResponse>
    suspend fun singIn(email: String, password: String): Flow<AuthResponse>
    suspend fun getCurrentUserName(): Flow<String>
    suspend fun getCurrentUserId(): Flow<String>

}