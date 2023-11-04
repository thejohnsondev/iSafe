package com.thejohnsondev.domain

import com.thejohnsondev.common.navigation.Screens
import com.thejohnsondev.data.AuthRepository
import com.thejohnsondev.datastore.DataStore
import javax.inject.Inject

class GetFirstScreenRouteUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStore: DataStore
) : GetFirstScreenRouteUseCase {
    override suspend operator fun invoke(): String {
        return if (isUserLoggedIn() && !isKeyEntered() && !isFromLogin()) {
            Screens.CreateEncryptionKeyScreen.name
        } else if (isUserLoggedIn() && !isKeyEntered() && isFromLogin()) {
            Screens.EnterEncryptionKeyScreen.name
        } else if (isUserLoggedIn() && isKeyEntered()) {
            Screens.HomeScreen.name
        } else {
            Screens.SignUpScreen.name
        }
    }

    private fun isUserLoggedIn(): Boolean = authRepository.isUserLoggedIn()

    private suspend fun isKeyEntered(): Boolean = dataStore.getUserKey().isNotEmpty()

    private suspend fun isFromLogin(): Boolean = dataStore.getIsFromLogin()
}