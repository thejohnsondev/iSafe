package com.thejohnsondev.domain

import com.thejohnsondev.common.awaitChannelFlow
import com.thejohnsondev.common.encrypt
import com.thejohnsondev.common.sendOrNothing
import com.thejohnsondev.model.UserDataResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CheckUserKeyCorrectUseCaseImpl @Inject constructor(
    private val getLocalUserData: GetLocalUserDataUseCase,
    private val getRemoteUserData: GetRemoteUserDataUseCase,
    private val coroutineScope: CoroutineScope
) : CheckUserKeyCorrectUseCase {
    override suspend fun invoke(key: ByteArray): Flow<Boolean> = awaitChannelFlow {
        val userId = getLocalUserData().id
        getRemoteUserData(userId.orEmpty()).collect {
            when (it) {
                is UserDataResponse.ResponseFailure -> {
                    coroutineScope.launch {
                        sendOrNothing(false)
                    }
                }

                is UserDataResponse.ResponseSuccess -> {
                    val actualUserSecret = it.userModel.userSecret
                    val generatedUserSecret = userId?.encrypt(key)
                    coroutineScope.launch {
                        sendOrNothing(actualUserSecret == generatedUserSecret)
                    }
                }
            }
        }
    }

}