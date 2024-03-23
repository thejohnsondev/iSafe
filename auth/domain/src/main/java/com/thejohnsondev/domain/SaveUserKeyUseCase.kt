package com.thejohnsondev.domain

import com.thejohnsondev.datastore.DataStore
import javax.inject.Inject

class SaveUserKeyUseCase @Inject constructor(
    private val dataStore: DataStore
) {
    suspend operator fun invoke(userKey: ByteArray) {
        dataStore.saveUserKey(userKey)
    }
}