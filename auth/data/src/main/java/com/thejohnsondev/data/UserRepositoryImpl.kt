package com.thejohnsondev.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thejohnsondev.common.PARAM_ID
import com.thejohnsondev.common.PARAM_USER_NAME
import com.thejohnsondev.common.PARAM_USER_SECRET
import com.thejohnsondev.common.USERS_DATA_DB_REF
import com.thejohnsondev.common.USERS_DB_REF
import com.thejohnsondev.common.awaitChannelFlow
import com.thejohnsondev.common.sendOrNothing
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.UserDataResponse
import com.thejohnsondev.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val coroutineScope: CoroutineScope,
) : UserRepository {


    override fun createUser(userModel: UserModel): Flow<DatabaseResponse> = awaitChannelFlow {
        firebaseDatabase.getReference(USERS_DB_REF)
            .child(userModel.id.orEmpty())
            .child(USERS_DATA_DB_REF)
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
            val userRef = firebaseDatabase.getReference(USERS_DB_REF)
                .child(userId)
                .child(USERS_DATA_DB_REF)

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
            .child(USERS_DATA_DB_REF)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userMap = snapshot.value as HashMap<String, String?>
                    val userModel = UserModel(
                        id = userMap[PARAM_ID],
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