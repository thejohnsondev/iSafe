package com.thejohnsondev.domain

import com.thejohnsondev.datastore.DataStore
import javax.inject.Inject

class SaveUserKeyUseCaseImpl @Inject constructor(
    private val dataStore: DataStore
) : SaveUserKeyUseCase {
    override suspend fun invoke(userKey: ByteArray) {
        dataStore.saveUserKey(userKey)
    }
}