package com.thejohnsondev.isafe.domain.use_cases.auth

import com.google.firebase.auth.FirebaseAuth
import com.thejohnsondev.isafe.data.local_data_source.DataStore
import com.thejohnsondev.isafe.presentation.navigation.Screens
import javax.inject.Inject

class GetFirstScreenRouteImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val dataStore: DataStore
) : GetFirstScreenRoute {
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

    private fun isUserLoggedIn(): Boolean = !firebaseAuth.currentUser?.email.isNullOrBlank()

    private suspend fun isKeyEntered(): Boolean = dataStore.getUserKey().isNotEmpty()

    private suspend fun isFromLogin(): Boolean = dataStore.getIsFromLogin()
}