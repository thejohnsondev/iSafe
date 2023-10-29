package com.thejohnsondev.domain

import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.model.UserModel
import javax.inject.Inject

class GetLocalUserDataUseCaseImpl @Inject constructor(
    private val dataStore: DataStore
) : GetLocalUserDataUseCase {
    override suspend fun invoke(): UserModel {
        return dataStore.getUserData()
    }
}