package com.thejohnsondev.isafe.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.thejohnsondev.isafe.domain.models.AuthResponse
import com.thejohnsondev.isafe.domain.repositories.AuthRepository
import com.thejohnsondev.isafe.utils.DEFAULT_USER_ID
import com.thejohnsondev.isafe.utils.DEFAULT_USER_NAME
import com.thejohnsondev.isafe.utils.awaitChannelFlow
import com.thejohnsondev.isafe.utils.sendOrNothing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val coroutineScope: CoroutineScope
) : AuthRepository {
    override suspend fun signUp(email: String, password: String): Flow<AuthResponse> =
        awaitChannelFlow {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    coroutineScope.launch {
                        sendOrNothing(AuthResponse.ResponseSuccess(it))
                    }
                }
                .addOnFailureListener {
                    coroutineScope.launch {
                        sendOrNothing(AuthResponse.ResponseFailure(it))
                    }
                }
        }

    override suspend fun singIn(email: String, password: String): Flow<AuthResponse> =
        awaitChannelFlow {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    coroutineScope.launch {
                        sendOrNothing(AuthResponse.ResponseSuccess(it))
                    }
                }
                .addOnFailureListener {
                    coroutineScope.launch {
                        sendOrNothing(AuthResponse.ResponseFailure(it))
                    }
                }
        }

    override suspend fun getCurrentUserName(): Flow<String> = awaitChannelFlow {
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
        if (!currentUserEmail.isNullOrEmpty()) {
            sendOrNothing(currentUserEmail.split("@").first())
        } else {
            sendOrNothing(DEFAULT_USER_NAME)
        }
    }

    override suspend fun getCurrentUserId(): Flow<String> = awaitChannelFlow {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (!currentUserId.isNullOrEmpty()) {
            sendOrNothing(currentUserId)
        } else {
            sendOrNothing(DEFAULT_USER_ID)
        }
    }
}