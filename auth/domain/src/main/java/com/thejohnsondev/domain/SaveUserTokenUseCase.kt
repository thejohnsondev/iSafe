package com.thejohnsondev.domain

import com.thejohnsondev.datastore.DataStore
import javax.inject.Inject

class SaveUserTokenUseCase @Inject constructor(
    private val dataStore: DataStore
) {

    suspend operator fun invoke(token: String) {
        dataStore.saveUserToken(token)
    }


}