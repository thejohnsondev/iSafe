package com.thejohnsondev.data

import arrow.core.Either
import com.thejohnsondev.common.awaitChannelFlow
import com.thejohnsondev.common.sendOrNothing
import com.thejohnsondev.database.local_datasource.LocalDataSource
import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.network.di.DotNetRemoteDataSource
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PasswordsRepositoryImpl @Inject constructor(
    @DotNetRemoteDataSource private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val dataStore: DataStore
) : PasswordsRepository {

    override fun getUserPasswords(): Flow<Either<ApiError, List<PasswordModel>>> = awaitChannelFlow {
        if (!dataStore.isFirstPasswordsLoad()) {
            send(Either.Right(localDataSource.getUserPasswords()))
        } else {
            dataStore.setIsFirstPasswordsLoad(false)
        }
        remoteDataSource.getUserPasswords().first().fold(
            ifLeft = { sendOrNothing(Either.Left(it)) },
            ifRight = {
                localDataSource.updatePasswords(it)
                sendOrNothing(Either.Right(it))
            }
        )
    }

    override fun createPassword(password: PasswordModel): Flow<Either<ApiError, PasswordModel>> = awaitChannelFlow {
        remoteDataSource.createPassword(password).first().fold(
            ifLeft = { sendOrNothing(Either.Left(it)) },
            ifRight = {
                localDataSource.createPassword(it)
                sendOrNothing(Either.Right(it))
            }
        )
    }

    override fun updatePassword(password: PasswordModel): Flow<Either<ApiError, Unit>> = awaitChannelFlow {
        remoteDataSource.updatePassword(password).first().fold(
            ifLeft = { sendOrNothing(Either.Left(it)) },
            ifRight = {
                localDataSource.updatePassword(it)
                sendOrNothing(Either.Right(Unit))
            }
        )
    }


    override fun updatePasswordsList(
        newPasswordList: List<PasswordModel>
    ): Flow<DatabaseResponse> =
        remoteDataSource.updatePasswordsList(newPasswordList)

    override fun deletePassword(passwordId: String): Flow<Either<ApiError, Unit>> = awaitChannelFlow {
        remoteDataSource.deletePassword(passwordId).first().fold(
            ifLeft = { sendOrNothing(Either.Left(it)) },
            ifRight = {
                localDataSource.deletePassword(passwordId)
                sendOrNothing(Either.Right(Unit))
            }
        )
    }

}