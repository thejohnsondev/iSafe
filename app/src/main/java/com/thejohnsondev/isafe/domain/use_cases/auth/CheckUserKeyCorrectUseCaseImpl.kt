package com.thejohnsondev.isafe.domain.use_cases.auth

import com.thejohnsondev.isafe.domain.models.UserDataResponse
import com.thejohnsondev.isafe.domain.use_cases.user.GetLocalUserDataUseCase
import com.thejohnsondev.isafe.domain.use_cases.user.GetRemoteUserDataUseCase
import com.thejohnsondev.isafe.utils.awaitChannelFlow
import com.thejohnsondev.isafe.utils.encrypt
import com.thejohnsondev.isafe.utils.sendOrNothing
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