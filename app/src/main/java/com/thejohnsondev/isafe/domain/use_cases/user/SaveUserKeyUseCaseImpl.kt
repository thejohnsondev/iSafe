package com.thejohnsondev.isafe.domain.use_cases.user

import com.thejohnsondev.isafe.data.local_data_source.DataStore
import javax.inject.Inject

class SaveUserKeyUseCaseImpl @Inject constructor(
    private val dataStore: DataStore
) : SaveUserKeyUseCase {
    override suspend fun invoke(userKey: ByteArray) {
        dataStore.saveUserKey(userKey)
    }
}