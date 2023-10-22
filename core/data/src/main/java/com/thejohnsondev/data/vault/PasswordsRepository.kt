package com.thejohnsondev.data.vault

import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.model.UserPasswordsResponse
import kotlinx.coroutines.flow.Flow

interface PasswordsRepository {
    fun getUserPasswords(userId: String): Flow<UserPasswordsResponse>
    fun createPassword(userId: String, password: PasswordModel): Flow<DatabaseResponse>
    fun updatePassword(userId: String, password: PasswordModel): Flow<DatabaseResponse>
    fun deletePassword(userId: String, passwordId: String): Flow<DatabaseResponse>
}