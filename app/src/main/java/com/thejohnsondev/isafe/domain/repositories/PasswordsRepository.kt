package com.thejohnsondev.isafe.domain.repositories

import com.thejohnsondev.isafe.domain.models.DatabaseResponse
import com.thejohnsondev.isafe.domain.models.PasswordModel
import com.thejohnsondev.isafe.domain.models.UserPasswordsResponse
import kotlinx.coroutines.flow.Flow

interface PasswordsRepository {
    fun getUserPasswords(userId: String): Flow<UserPasswordsResponse>
    fun createPassword(userId: String, password: PasswordModel): Flow<DatabaseResponse>
    fun updatePassword(userId: String, password: PasswordModel): Flow<DatabaseResponse>
    fun deletePassword(userId: String, passwordId: String): Flow<DatabaseResponse>
}