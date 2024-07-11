package com.thejohnsondev.network.remote_datasource.dotnet

import arrow.core.Either
import com.thejohnsondev.common.awaitChannelFlow
import com.thejohnsondev.common.sendOrNothing
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.ChangePasswordBody
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.model.auth.AuthBody
import com.thejohnsondev.model.auth.AuthResponse
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ISafeDotNetRemoteDataSource @Inject constructor(
    private val api: ISafeDotNetApi
) : RemoteDataSource {

    override fun getUserPasswords(): Flow<Either<ApiError, List<PasswordModel>>> =
        awaitChannelFlow {
            sendOrNothing(api.getPasswords())
        }

    override fun createPassword(
        passwordModel: PasswordModel
    ): Flow<Either<ApiError, PasswordModel>> =
        awaitChannelFlow {
            sendOrNothing(api.addPassword(passwordModel))
        }

    override fun updatePassword(
        passwordModel: PasswordModel
    ): Flow<Either<ApiError, PasswordModel>> =
        awaitChannelFlow {
            sendOrNothing(api.updatePassword(passwordModel.id, passwordModel))
        }

    override fun updatePasswordsList(
        newPasswordList: List<PasswordModel>
    ): Flow<DatabaseResponse> = awaitChannelFlow {
        // TODO: not implemented yet
    }

    override fun deletePassword(passwordId: String): Flow<Either<ApiError, Unit>> =
        awaitChannelFlow {
            sendOrNothing(api.deletePassword(passwordId))
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

    override suspend fun deleteAccount(): Flow<Either<ApiError, Unit>> = awaitChannelFlow {
        sendOrNothing(api.deleteAccount())
    }

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): Flow<Either<ApiError, Unit>> = awaitChannelFlow {
        sendOrNothing(api.changePassword(ChangePasswordBody(oldPassword, newPassword)))
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

    override fun deleteNote(noteId: String): Flow<Either<ApiError, Unit>> = awaitChannelFlow {
        sendOrNothing(api.deleteNote(noteId))
    }

}