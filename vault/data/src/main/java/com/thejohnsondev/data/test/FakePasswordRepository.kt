package com.thejohnsondev.data.test

import com.thejohnsondev.data.PasswordsRepository
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.model.UserPasswordsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakePasswordRepository: PasswordsRepository {

    val passwords = HashMap<String, List<PasswordModel>>()

    init {
        passwords["1"] = user1TestPasswords
        passwords["2"] = user2TestPasswords
    }
    override fun getUserPasswords(userId: String): Flow<UserPasswordsResponse> = flow {
        emit(UserPasswordsResponse.ResponseSuccess(passwords[userId].orEmpty()))
    }

    override fun createPassword(userId: String, password: PasswordModel): Flow<DatabaseResponse> = flow {
        val currentList = passwords[userId]?.toMutableList()
        currentList?.add(password)
        passwords[userId] = currentList?.toList().orEmpty()
        emit(DatabaseResponse.ResponseSuccess)
    }

    override fun updatePassword(userId: String, password: PasswordModel): Flow<DatabaseResponse> = flow {
        val currentList = passwords[userId]?.toMutableList()
        currentList?.removeIf { it.id == password.id }
        currentList?.add(password)
        passwords[userId] = currentList?.toList().orEmpty()
        emit(DatabaseResponse.ResponseSuccess)
    }

    override fun deletePassword(userId: String, passwordId: String): Flow<DatabaseResponse> = flow {
        val currentList = passwords[userId]?.toMutableList()
        currentList?.removeIf { it.id == passwordId }
        passwords[userId] = currentList?.toList().orEmpty()
        emit(DatabaseResponse.ResponseSuccess)
    }

    companion object {
        val user1TestPasswords = listOf(
            PasswordModel(
                "11",
                "org11",
                null,
                "title11",
                "password11",
            ),
            PasswordModel(
                "12",
                "org12",
                null,
                "title12",
                "password12",
            ),
            PasswordModel(
                "13",
                "org13",
                null,
                "title13",
                "password13",
            )
        )
        val user2TestPasswords = listOf(
            PasswordModel(
                "21",
                "org21",
                null,
                "title21",
                "password21",
            ),
            PasswordModel(
                "22",
                "org22",
                null,
                "title22",
                "password22",
            ),
            PasswordModel(
                "23",
                "org23",
                null,
                "title23",
                "password23",
            )
        )
    }
}