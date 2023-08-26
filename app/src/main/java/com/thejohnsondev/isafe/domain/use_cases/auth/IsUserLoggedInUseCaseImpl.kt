package com.thejohnsondev.isafe.domain.use_cases.auth

import com.google.firebase.auth.FirebaseAuth
import com.thejohnsondev.isafe.data.local_data_source.DataStore
import javax.inject.Inject

class IsUserLoggedInUseCaseImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val dataStore: DataStore
): IsUserLoggedInUseCase {
    override suspend operator fun invoke(): Boolean {
        return !firebaseAuth.currentUser?.email.isNullOrBlank() && dataStore.getUserSecret() != null
    }
}