package com.thejohnsondev.domain

import com.thejohnsondev.datastore.DataStore
import javax.inject.Inject

class SaveUserEmailUseCase @Inject constructor(
    private val dataStore: DataStore
) {
    suspend operator fun invoke(email: String) {
        dataStore.saveUserEmail(email)
    }
}
