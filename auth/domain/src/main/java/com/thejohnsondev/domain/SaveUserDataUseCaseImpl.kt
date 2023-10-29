package com.thejohnsondev.domain

import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.model.UserModel
import javax.inject.Inject

class SaveUserDataUseCaseImpl @Inject constructor(
    private val dataStore: DataStore
) : SaveUserDataUseCase {
    override suspend fun invoke(userModel: UserModel, isFromLogin: Boolean) {
        dataStore.saveUserData(userModel)
        dataStore.setIsFromLogin(isFromLogin)
    }
}