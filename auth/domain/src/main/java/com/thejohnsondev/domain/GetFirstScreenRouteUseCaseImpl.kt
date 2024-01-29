package com.thejohnsondev.domain

import com.thejohnsondev.common.navigation.Screens
import com.thejohnsondev.data.AuthRepository
import javax.inject.Inject

class GetFirstScreenRouteUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : GetFirstScreenRouteUseCase {
    override suspend operator fun invoke(): String {
        return if (isUserLoggedIn()) {
            Screens.HomeScreen.name
        } else {
            Screens.SignUpScreen.name
        }
    }

    private fun isUserLoggedIn(): Boolean = authRepository.isUserLoggedIn()
}