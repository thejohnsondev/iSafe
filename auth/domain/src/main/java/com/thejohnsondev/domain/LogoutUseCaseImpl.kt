package com.thejohnsondev.domain

import com.thejohnsondev.data.AuthRepository
import com.thejohnsondev.datastore.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class LogoutUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val coroutineScope: CoroutineScope,
    private val dataStore: DataStore
) : LogoutUseCase {
    override suspend fun invoke() {
        coroutineScope.launch {
            authRepository.signOut()
            dataStore.clearUserData()
        }
    }
}