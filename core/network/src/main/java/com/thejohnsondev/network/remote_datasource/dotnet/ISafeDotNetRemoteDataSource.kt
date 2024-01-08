package com.thejohnsondev.network.remote_datasource.dotnet

import arrow.core.Either
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thejohnsondev.common.DEFAULT_ID
import com.thejohnsondev.common.DEFAULT_USER_ID
import com.thejohnsondev.common.DEFAULT_USER_NAME
import com.thejohnsondev.common.NOTES_DB_REF
import com.thejohnsondev.common.PARAM_ADDITIONAL_FIELDS
import com.thejohnsondev.common.PARAM_CATEGORY
import com.thejohnsondev.common.PARAM_DESCRIPTION
import com.thejohnsondev.common.PARAM_ID
import com.thejohnsondev.common.PARAM_ORGANIZATION
import com.thejohnsondev.common.PARAM_ORGANIZATION_LOGO
import com.thejohnsondev.common.PARAM_PASSWORD
import com.thejohnsondev.common.PARAM_TITLE
import com.thejohnsondev.common.PARAM_USER_NAME
import com.thejohnsondev.common.PARAM_USER_SECRET
import com.thejohnsondev.common.PARAM_VALUE
import com.thejohnsondev.common.PASSWORDS_DB_REF
import com.thejohnsondev.common.USERS_DATA_DB_REF
import com.thejohnsondev.common.USERS_DB_REF
import com.thejohnsondev.common.awaitChannelFlow
import com.thejohnsondev.common.decrypt
import com.thejohnsondev.common.encrypt
import com.thejohnsondev.common.sendOrNothing
import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.model.AdditionalField
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.model.UserDataResponse
import com.thejohnsondev.model.UserModel
import com.thejohnsondev.model.UserNotesResponse
import com.thejohnsondev.model.UserPasswordsResponse
import com.thejohnsondev.model.auth.AuthBody
import com.thejohnsondev.model.auth.AuthResponse
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ISafeDotNetRemoteDataSource @Inject constructor(
    private val api: ISafeDotNetApi,
    private val coroutineScope: CoroutineScope,
    private val firebaseDatabase: FirebaseDatabase, //todo temporar
    private val firebaseAuth: FirebaseAuth, //todo temporar
    private val dataStore: DataStore
) : RemoteDataSource {

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
                                                id = it.get(PARAM_ID).orEmpty(),
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
                                        id = (it as HashMap<String, String>?)?.get(
                                            PARAM_ID
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
                            id = it.get(PARAM_ID).orEmpty(),
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
            val encryptedPassword = PasswordModel(
                id = password.id,
                organization = password.organization.encrypt(getKey()),
                organizationLogo = password.organizationLogo?.encrypt(getKey()),
                title = password.title.encrypt(getKey()),
                password = password.password.encrypt(getKey()),
                additionalFields = password.additionalFields.map {
                    AdditionalField(
                        id = it.id,
                        title = it.title.encrypt(getKey()),
                        value = it.value.encrypt(getKey())
                    )
                }
            )
            firebaseDatabase.getReference(USERS_DB_REF)
                .child(userId)
                .child(PASSWORDS_DB_REF)
                .child(password.id)
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
                .child(password.id)

            val newPasswordValues = mapOf(
                PARAM_ID to password.id,
                PARAM_ORGANIZATION to password.organization.encrypt(getKey()),
                PARAM_ORGANIZATION_LOGO to password.organizationLogo?.encrypt(getKey()),
                PARAM_TITLE to password.title.encrypt(getKey()),
                PARAM_PASSWORD to password.password.encrypt(getKey()),
                PARAM_ADDITIONAL_FIELDS to password.additionalFields.map {
                    mapOf(
                        PARAM_ID to it.id,
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

    override fun updatePasswordsList(
        userId: String,
        newPasswordList: List<PasswordModel>
    ): Flow<DatabaseResponse> = awaitChannelFlow {
        val passwordsRef = firebaseDatabase.getReference(USERS_DB_REF)
            .child(userId)
            .child(PASSWORDS_DB_REF)
        val encryptedPasswordsMap = mutableMapOf<String, PasswordModel>()

        newPasswordList.forEach { password ->
            encryptedPasswordsMap[password.id] = PasswordModel(
                id = password.id,
                organization = password.organization.encrypt(getKey()),
                organizationLogo = password.organizationLogo?.encrypt(getKey()),
                title = password.title.encrypt(getKey()),
                password = password.password.encrypt(getKey()),
                additionalFields = password.additionalFields.map {
                    AdditionalField(
                        id = it.id,
                        title = it.title.encrypt(getKey()),
                        value = it.value.encrypt(getKey())
                    )
                }
            )
        }

        passwordsRef.removeValue()

        passwordsRef
            .updateChildren(encryptedPasswordsMap.toMap())
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

    override suspend fun signUp(
        email: String,
        password: String
    ): Flow<Either<ApiError, AuthResponse>> =
        awaitChannelFlow {
            sendOrNothing(
                api.signUp(AuthBody(email, password))
            )
        }

    override suspend fun singIn(email: String, password: String): Flow<Either<ApiError, AuthResponse>> =
        awaitChannelFlow {
            sendOrNothing(
                api.login(AuthBody(email, password))
            )
        }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun getCurrentUserName(): Flow<String> = awaitChannelFlow {
        val currentUserEmail = firebaseAuth.currentUser?.email
        if (!currentUserEmail.isNullOrEmpty()) {
            sendOrNothing(currentUserEmail.split("@").first())
        } else {
            sendOrNothing(DEFAULT_USER_NAME)
        }
    }

    override suspend fun getCurrentUserId(): Flow<String> = awaitChannelFlow {
        val currentUserId = firebaseAuth.currentUser?.uid
        if (!currentUserId.isNullOrEmpty()) {
            sendOrNothing(currentUserId)
        } else {
            sendOrNothing(DEFAULT_USER_ID)
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return !firebaseAuth.currentUser?.email.isNullOrBlank()
    }

    override fun getUserNotes(userId: String): Flow<UserNotesResponse> = awaitChannelFlow {
        firebaseDatabase.getReference(USERS_DB_REF)
            .child(userId)
            .child(NOTES_DB_REF)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    coroutineScope.launch {
                        val noteMap = snapshot.value as HashMap<String, HashMap<String, String?>?>?
                        val notesList = mutableListOf<NoteModel>()
                        if (noteMap == null) {
                            sendOrNothing(UserNotesResponse.ResponseSuccess(notesList))
                            close()
                        }
                        noteMap?.values?.forEach {
                            it?.let {
                                notesList.add(
                                    NoteModel(
                                        id = it[PARAM_ID] ?: DEFAULT_ID,
                                        title = it[PARAM_TITLE]?.decrypt(getKey()).orEmpty(),
                                        description = it[PARAM_DESCRIPTION]?.decrypt(getKey())
                                            .orEmpty(),
                                        category = it[PARAM_CATEGORY]?.decrypt(getKey()).orEmpty()
                                    )
                                )
                            }
                        }
                        sendOrNothing(UserNotesResponse.ResponseSuccess(notesList))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    coroutineScope.launch {
                        sendOrNothing(UserNotesResponse.ResponseFailure(error.toException()))
                    }
                }
            })
    }

    override fun createNote(userId: String, note: NoteModel): Flow<DatabaseResponse> =
        awaitChannelFlow {
            if (note.title.isEmpty() && note.description.isEmpty()) {
                sendOrNothing(DatabaseResponse.ResponseFailure(Exception("Your note is empty")))
                close()
            }
            val encryptedNote = NoteModel(
                id = note.id,
                title = note.title.encrypt(getKey()),
                description = note.description.encrypt(getKey()),
                category = note.category.encrypt(getKey())
            )
            firebaseDatabase.getReference(USERS_DB_REF)
                .child(userId)
                .child(NOTES_DB_REF)
                .child(encryptedNote.id)
                .setValue(encryptedNote)
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

    override fun updateNote(userId: String, note: NoteModel): Flow<DatabaseResponse> =
        awaitChannelFlow {
            val noteRef = firebaseDatabase.getReference(USERS_DB_REF)
                .child(userId)
                .child(NOTES_DB_REF)
                .child(note.id)

            val newNoteValues = mapOf(
                PARAM_ID to note.id,
                PARAM_TITLE to note.title.encrypt(getKey()),
                PARAM_DESCRIPTION to note.description.encrypt(getKey()),
                PARAM_CATEGORY to note.category.encrypt(getKey())
            )
            noteRef.updateChildren(newNoteValues)
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

    override fun deleteNote(userId: String, noteId: Int): Flow<DatabaseResponse> =
        awaitChannelFlow {
            firebaseDatabase.getReference(USERS_DB_REF)
                .child(userId)
                .child(NOTES_DB_REF)
                .child(noteId.toString())
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