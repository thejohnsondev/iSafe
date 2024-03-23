package com.thejohnsondev.data

import arrow.core.Either
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.model.UserPasswordsResponse
import kotlinx.coroutines.flow.Flow

interface PasswordsRepository {
    fun getUserPasswords(): Flow<Either<ApiError, List<PasswordModel>>>
    fun createPassword(password: PasswordModel): Flow<Either<ApiError, PasswordModel>>
    fun updatePassword(password: PasswordModel): Flow<Either<ApiError, Unit>>
    fun updatePasswordsList(newPasswordList: List<PasswordModel>): Flow<DatabaseResponse>
    fun deletePassword(passwordId: String): Flow<Either<ApiError, Unit>>
}