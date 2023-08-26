package com.thejohnsondev.isafe.domain.use_cases.user

import com.thejohnsondev.isafe.data.local_data_source.DataStore
import com.thejohnsondev.isafe.domain.models.UserModel
import javax.inject.Inject

class SaveUserDataUseCaseImpl @Inject constructor(
    private val dataStore: DataStore
): SaveUserDataUseCase {
    override suspend fun invoke(userModel: UserModel, isFromLogin: Boolean) {
        dataStore.saveUserData(userModel)
        dataStore.setIsFromLogin(isFromLogin)
    }
}