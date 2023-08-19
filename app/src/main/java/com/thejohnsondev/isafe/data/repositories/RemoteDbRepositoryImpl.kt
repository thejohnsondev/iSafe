package com.thejohnsondev.isafe.data.repositories

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thejohnsondev.isafe.domain.models.DatabaseResponse
import com.thejohnsondev.isafe.domain.models.UserDataResponse
import com.thejohnsondev.isafe.domain.models.UserModel
import com.thejohnsondev.isafe.domain.repositories.RemoteDbRepository
import com.thejohnsondev.isafe.utils.PARAM_USER_ID
import com.thejohnsondev.isafe.utils.PARAM_USER_NAME
import com.thejohnsondev.isafe.utils.PARAM_USER_SECRET
import com.thejohnsondev.isafe.utils.USERS_DB_REF
import com.thejohnsondev.isafe.utils.awaitChannelFlow
import com.thejohnsondev.isafe.utils.sendOrNothing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RemoteDbRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val coroutineScope: CoroutineScope
) : RemoteDbRepository {
    override fun createUser(userModel: UserModel): Flow<DatabaseResponse> = awaitChannelFlow {
        firebaseDatabase.getReference(USERS_DB_REF)
            .child(userModel.id.orEmpty())
            .setValue(userModel)
            .addOnSuccessListener {
                coroutineScope.launch {
                    sendOrNothing(DatabaseResponse.ResponseSuccess)
                }
            }.addOnFailureListener {
                coroutineScope.launch {
                    sendOrNothing(DatabaseResponse.ResponseFailure(it))
                }
            }
    }

    override fun updateUserSecret(userId: String, userSecret: String): Flow<DatabaseResponse> =
        awaitChannelFlow {
            val userRef = firebaseDatabase.getReference(USERS_DB_REF).child(userId)
            val valuesMap = mapOf(
                PARAM_USER_SECRET to userSecret
            )
            userRef.updateChildren(valuesMap)
                .addOnSuccessListener {
                    coroutineScope.launch {
                        sendOrNothing(DatabaseResponse.ResponseSuccess)
                    }
                }.addOnFailureListener {
                    coroutineScope.launch {
                        sendOrNothing(DatabaseResponse.ResponseFailure(it))
                    }
                }
        }

    override fun getUserData(userId: String): Flow<UserDataResponse> = awaitChannelFlow {
        firebaseDatabase.getReference(USERS_DB_REF)
            .child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userMap = snapshot.value as HashMap<String, String?>
                    val userModel = UserModel(
                        id = userMap[PARAM_USER_ID],
                        name = userMap[PARAM_USER_NAME],
                        userSecret = userMap[PARAM_USER_SECRET]
                    )
                    coroutineScope.launch {
                        sendOrNothing(UserDataResponse.ResponseSuccess(userModel))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    coroutineScope.launch {
                        sendOrNothing(UserDataResponse.ResponseFailure(error.toException()))
                    }
                }

            })
    }
}