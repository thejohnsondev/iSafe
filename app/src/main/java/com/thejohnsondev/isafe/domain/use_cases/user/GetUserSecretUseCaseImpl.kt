package com.thejohnsondev.isafe.domain.use_cases.user

import com.thejohnsondev.isafe.data.local_data_source.DataStore
import javax.inject.Inject

class GetUserSecretUseCaseImpl @Inject constructor(
    private val dataStore: DataStore
): GetUserSecretUseCase {
    override suspend fun invoke(): String? {
        return dataStore.getUserSecret()
    }
}