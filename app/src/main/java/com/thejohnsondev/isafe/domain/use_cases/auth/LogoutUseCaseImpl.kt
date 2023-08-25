package com.thejohnsondev.isafe.domain.use_cases.auth

import com.google.firebase.auth.FirebaseAuth
import com.thejohnsondev.isafe.utils.awaitChannelFlow
import com.thejohnsondev.isafe.utils.sendOrNothing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LogoutUseCaseImpl(
    private val firebaseAuth: FirebaseAuth,
    private val coroutineScope: CoroutineScope
): LogoutUseCase {
    override suspend fun invoke(): Flow<Boolean> = awaitChannelFlow {
        coroutineScope.launch {
            firebaseAuth.signOut()
            sendOrNothing(true)
        }
    }
}