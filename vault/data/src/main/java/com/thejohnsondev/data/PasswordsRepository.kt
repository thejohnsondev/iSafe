package com.thejohnsondev.data

import arrow.core.Either
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.model.UserPasswordsResponse
import kotlinx.coroutines.flow.Flow

interface PasswordsRepository {
    fun getUserPasswords(userId: String): Flow<Either<ApiError, List<PasswordModel>>>
    fun createPassword(userId: String, password: PasswordModel): Flow<Either<ApiError, PasswordModel>>
    fun updatePassword(userId: String, password: PasswordModel): Flow<Either<ApiError, Unit>>
    fun updatePasswordsList(userId: String, newPasswordList: List<PasswordModel>): Flow<DatabaseResponse>
    fun deletePassword(userId: String, passwordId: String): Flow<Either<ApiError, Unit>>
}