package com.thejohnsondev.isafe.data.repositories

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thejohnsondev.isafe.data.local_data_source.DataStore
import com.thejohnsondev.isafe.domain.models.AdditionalField
import com.thejohnsondev.isafe.domain.models.DatabaseResponse
import com.thejohnsondev.isafe.domain.models.PasswordModel
import com.thejohnsondev.isafe.domain.models.UserPasswordsResponse
import com.thejohnsondev.isafe.domain.repositories.PasswordsRepository
import com.thejohnsondev.isafe.utils.PARAM_ADDITIONAL_FIELDS
import com.thejohnsondev.isafe.utils.PARAM_ORGANIZATION
import com.thejohnsondev.isafe.utils.PARAM_ORGANIZATION_LOGO
import com.thejohnsondev.isafe.utils.PARAM_PASSWORD
import com.thejohnsondev.isafe.utils.PARAM_TIMESTAMP
import com.thejohnsondev.isafe.utils.PARAM_TITLE
import com.thejohnsondev.isafe.utils.PARAM_VALUE
import com.thejohnsondev.isafe.utils.PASSWORDS_DB_REF
import com.thejohnsondev.isafe.utils.USERS_DB_REF
import com.thejohnsondev.isafe.utils.awaitChannelFlow
import com.thejohnsondev.isafe.utils.decrypt
import com.thejohnsondev.isafe.utils.encrypt
import com.thejohnsondev.isafe.utils.sendOrNothing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PasswordsRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val coroutineScope: CoroutineScope,
    private val dataStore: DataStore
) : PasswordsRepository {

    private suspend fun getKey(): ByteArray {
        return dataStore.getUserKey()
    }

    override fun getUserPasswords(userId: String): Flow<UserPasswordsResponse> = awaitChannelFlow {
        firebaseDatabase.getReference(USERS_DB_REF)
            .child(userId)
            .child(PASSWORDS_DB_REF)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    coroutineScope.launch {
                        val passwordMap = snapshot.value as HashMap<String, Any?>?
                        val passwordsList = mutableListOf<PasswordModel>()
                        val additionalFieldsList = mutableListOf<AdditionalField>()
                        if (passwordMap == null) {
                            sendOrNothing(UserPasswordsResponse.ResponseSuccess(passwordsList))
                            close()
                        }
                        passwordMap?.values?.forEach {
                            (it as HashMap<String, Any>).values.forEach {
                                if (it is List<*>) {
                                    (it as List<HashMap<String, String>>).forEach {
                                        additionalFieldsList.add(
                                            AdditionalField(
                                                timeStamp = it.get(PARAM_TIMESTAMP).orEmpty(),
                                                title = it.get(PARAM_TITLE)?.decrypt(getKey())
                                                    .orEmpty(),
                                                value = it.get(PARAM_VALUE)?.decrypt(getKey())
                                                    .orEmpty()
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        passwordMap?.values?.forEach {
                            (it as HashMap<String, Any?>?).let {
                                passwordsList.add(
                                    PasswordModel(
                                        timestamp = (it as HashMap<String, String>?)?.get(
                                            PARAM_TIMESTAMP
                                        ).orEmpty(),
                                        organization = (it as HashMap<String, String>?)?.get(
                                            PARAM_ORGANIZATION
                                        )?.decrypt(getKey()).orEmpty(),
                                        organizationLogo = (it as HashMap<String, String>?)?.get(
                                            PARAM_ORGANIZATION_LOGO
                                        )?.decrypt(getKey()).orEmpty(),
                                        title = (it as HashMap<String, String>?)?.get(PARAM_TITLE)
                                            ?.decrypt(getKey()).orEmpty(),
                                        password = (it as HashMap<String, String>?)?.get(
                                            PARAM_PASSWORD
                                        )?.decrypt(getKey()).orEmpty(),
                                        additionalFields = getAdditionalFields(it)
                                    )
                                )
                            }
                        }

                        sendOrNothing(UserPasswordsResponse.ResponseSuccess(passwordsList))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    coroutineScope.launch {
                        sendOrNothing(UserPasswordsResponse.ResponseFailure(error.toException()))
                    }
                }

            })
    }

    private suspend fun getAdditionalFields(map: HashMap<String, Any?>?): List<AdditionalField> {
        val additionalFieldsList = mutableListOf<AdditionalField>()
        map?.values?.forEach {
            if (it is List<*>) {
                (it as List<HashMap<String, String>>).forEach {
                    additionalFieldsList.add(
                        AdditionalField(
                            timeStamp = it.get(PARAM_TIMESTAMP).orEmpty(),
                            title = it.get(PARAM_TITLE)?.decrypt(getKey()).orEmpty(),
                            value = it.get(PARAM_VALUE)?.decrypt(getKey()).orEmpty()
                        )
                    )
                }
            }
        }
        return additionalFieldsList
    }

    override fun createPassword(userId: String, password: PasswordModel): Flow<DatabaseResponse> =
        awaitChannelFlow {
            if (password.organization.isEmpty() && password.title.isEmpty() && password.password.isEmpty()) {
                sendOrNothing(DatabaseResponse.ResponseFailure(Exception("Your password is empty")))
                close()
            }
            val encryptedPassword = PasswordModel(
                timestamp = password.timestamp,
                organization = password.organization.encrypt(getKey()),
                organizationLogo = password.organizationLogo?.encrypt(getKey()),
                title = password.title.encrypt(getKey()),
                password = password.password.encrypt(getKey()),
                additionalFields = password.additionalFields.map {
                    AdditionalField(
                        timeStamp = it.timeStamp,
                        title = it.title.encrypt(getKey()),
                        value = it.value.encrypt(getKey())
                    )
                }
            )
            firebaseDatabase.getReference(USERS_DB_REF)
                .child(userId)
                .child(PASSWORDS_DB_REF)
                .child(password.timestamp)
                .setValue(encryptedPassword)
                .addOnSuccessListener {
                    coroutineScope.launch {
                        sendOrNothing(DatabaseResponse.ResponseSuccess)
                    }
                }
                .addOnFailureListener {
                    coroutineScope.launch {
                        sendOrNothing(DatabaseResponse.ResponseFailure(it))
                    }
                }
        }

    override fun updatePassword(userId: String, password: PasswordModel): Flow<DatabaseResponse> =
        awaitChannelFlow {
            val passwordRef = firebaseDatabase.getReference(USERS_DB_REF)
                .child(userId)
                .child(PASSWORDS_DB_REF)
                .child(password.timestamp)

            val newPasswordValues = mapOf(
                PARAM_TIMESTAMP to password.timestamp,
                PARAM_ORGANIZATION to password.organization.encrypt(getKey()),
                PARAM_ORGANIZATION_LOGO to password.organizationLogo?.encrypt(getKey()),
                PARAM_TITLE to password.title.encrypt(getKey()),
                PARAM_PASSWORD to password.password.encrypt(getKey()),
                PARAM_ADDITIONAL_FIELDS to password.additionalFields.map {
                    mapOf(
                        PARAM_TIMESTAMP to it.timeStamp,
                        PARAM_TITLE to it.title.encrypt(getKey()),
                        PARAM_VALUE to it.value.encrypt(getKey())
                    )
                }
            )
            passwordRef.updateChildren(newPasswordValues)
                .addOnSuccessListener {
                    coroutineScope.launch {
                        sendOrNothing(DatabaseResponse.ResponseSuccess)
                    }
                }
                .addOnFailureListener {
                    coroutineScope.launch {
                        sendOrNothing(DatabaseResponse.ResponseFailure(it))
                    }
                }
        }

    override fun deletePassword(userId: String, passwordId: String): Flow<DatabaseResponse> =
        awaitChannelFlow {
            firebaseDatabase.getReference(USERS_DB_REF)
                .child(userId)
                .child(PASSWORDS_DB_REF)
                .child(passwordId)
                .removeValue()
                .addOnSuccessListener {
                    coroutineScope.launch {
                        sendOrNothing(DatabaseResponse.ResponseSuccess)
                    }
                }
                .addOnFailureListener {
                    coroutineScope.launch {
                        sendOrNothing(DatabaseResponse.ResponseFailure(it))
                    }
                }
        }
}