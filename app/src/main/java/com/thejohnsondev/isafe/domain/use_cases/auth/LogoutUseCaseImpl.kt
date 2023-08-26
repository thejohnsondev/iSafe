package com.thejohnsondev.isafe.domain.use_cases.auth

import com.google.firebase.auth.FirebaseAuth
import com.thejohnsondev.isafe.data.local_data_source.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class LogoutUseCaseImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val coroutineScope: CoroutineScope,
    private val dataStore: DataStore
) : LogoutUseCase {
    override suspend fun invoke() {
        coroutineScope.launch {
            firebaseAuth.signOut()
            dataStore.clearUserData()
        }
    }
}