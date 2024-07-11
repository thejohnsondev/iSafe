package com.thejohnsondev.data

import arrow.core.Either
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.auth.AuthResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeAuthRepository: AuthRepository {
    override suspend fun signUp(
        email: String,
        password: String
    ): Flow<Either<ApiError, AuthResponse>> = flowOf(Either.Right(AuthResponse("fake_token")))

    override suspend fun singIn(
        email: String,
        password: String
    ): Flow<Either<ApiError, AuthResponse>> = flowOf(Either.Right(AuthResponse("fake_token")))

    override suspend fun signOut() {
        // do nothing
    }

    override fun isUserLoggedIn(): Boolean = true

    override suspend fun deleteAccount(): Flow<Either<ApiError, Unit>> = flowOf(Either.Right(Unit))
    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): Flow<Either<ApiError, Unit>> = flowOf(Either.Right(Unit))
}