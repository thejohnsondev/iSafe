package com.thejohnsondev.domain

import com.thejohnsondev.data.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun invoke() {
        authRepository.signOut()
    }
}