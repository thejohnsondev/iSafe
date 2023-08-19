package com.thejohnsondev.isafe.domain.use_cases.user

import com.thejohnsondev.isafe.data.local_data_source.DataStore
import com.thejohnsondev.isafe.domain.models.UserModel
import javax.inject.Inject

class GetLocalUserDataUseCaseImpl @Inject constructor(
    private val dataStore: DataStore
): GetLocalUserDataUseCase {
    override suspend fun invoke(): UserModel {
        return dataStore.getUserData()
    }
}