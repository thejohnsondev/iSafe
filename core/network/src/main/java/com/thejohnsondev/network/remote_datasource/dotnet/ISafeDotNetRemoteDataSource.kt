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
    private val dataStore: DataStore
) : RemoteDataSource {

    private suspend fun getKey(): ByteArray {
        return dataStore.getUserKey()
    }

    override fun getUserPasswords(userId: String): Flow<Either<ApiError, List<PasswordModel>>> =
        awaitChannelFlow {
            sendOrNothing(api.getPasswords())
        }

    override fun createPassword(
        userId: String,
        password: PasswordModel
    ): Flow<Either<ApiError, PasswordModel>> =
        awaitChannelFlow {
            sendOrNothing(api.addPassword(password))
        }

    override fun updatePassword(
        userId: String,
        password: PasswordModel
    ): Flow<Either<ApiError, Unit>> =
        awaitChannelFlow {
            sendOrNothing(api.updatePassword(password.id, password))
        }

    override fun updatePasswordsList(
        userId: String,
        newPasswordList: List<PasswordModel>
    ): Flow<DatabaseResponse> = awaitChannelFlow {

    }

    override fun deletePassword(userId: String, passwordId: String): Flow<Either<ApiError, Unit>> =
        awaitChannelFlow {
            sendOrNothing(api.deletePassword(passwordId))
        }


    override fun createUser(userModel: UserModel): Flow<DatabaseResponse> = awaitChannelFlow {

    }

    override fun getUserData(userId: String): Flow<UserDataResponse> = awaitChannelFlow {

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

    override suspend fun singIn(
        email: String,
        password: String
    ): Flow<Either<ApiError, AuthResponse>> =
        awaitChannelFlow {
            sendOrNothing(
                api.login(AuthBody(email, password))
            )
        }

    override suspend fun signOut() {
    }

    override suspend fun getCurrentUserName(): Flow<String> = awaitChannelFlow {

    }

    override suspend fun getCurrentUserId(): Flow<String> = awaitChannelFlow {

    }

    override fun isUserLoggedIn(): Boolean {
        return false
    }

    override fun deleteAccount(): Flow<Either<ApiError, Unit>> = awaitChannelFlow {
        sendOrNothing(api.deleteAccount())
    }

    override fun getNotes(): Flow<Either<ApiError, List<NoteModel>>> = awaitChannelFlow {
        sendOrNothing(api.getNotes())
    }

    override fun createNote(noteModel: NoteModel): Flow<Either<ApiError, NoteModel>> = awaitChannelFlow {
        sendOrNothing(api.createNote(noteModel))
    }

    override fun updateNote(noteModel: NoteModel): Flow<Either<ApiError, Unit>> = awaitChannelFlow {
        sendOrNothing(api.updateNote(noteModel.id, noteModel))
    }

    override fun deleteNote(id: String): Flow<Either<ApiError, Unit>> = awaitChannelFlow {
        sendOrNothing(api.deleteNote(id))
    }

}