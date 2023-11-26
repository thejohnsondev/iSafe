package com.thejohnsondev.domain

import com.google.common.truth.Truth
import com.thejohnsondev.data.PasswordsRepository
import com.thejohnsondev.data.test.FakePasswordRepository
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.UserPasswordsResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule

@RunWith(MockitoJUnitRunner::class)
class DeletePasswordUseCaseTest {

    private lateinit var passwordsRepository: PasswordsRepository
    private lateinit var deletePasswordsUseCase: DeletePasswordUseCaseImpl
    private lateinit var getAllPasswordsUseCase: GetAllPasswordsUseCaseImpl

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Before
    fun setUp() {
        passwordsRepository = FakePasswordRepository()
        deletePasswordsUseCase = DeletePasswordUseCaseImpl(passwordsRepository)
        getAllPasswordsUseCase = GetAllPasswordsUseCaseImpl(passwordsRepository)
    }

    @Test
    fun givenValidUserAndPasswordId_returnsSuccess() = runBlocking {
        val result = deletePasswordsUseCase("1", "11").first()
        Truth.assertThat(result).isInstanceOf(DatabaseResponse.ResponseSuccess::class.java)

    }

    @Test
    fun givenValidUserAndPasswordId_removesPassword() = runBlocking {
        val initialPasswordsList = getAllPasswordsUseCase("1").first()
        val result = deletePasswordsUseCase("1", "11").first()

        val currentPasswordsList = getAllPasswordsUseCase("1").first()

        Truth.assertThat(result).isInstanceOf(DatabaseResponse.ResponseSuccess::class.java)
        if (currentPasswordsList is UserPasswordsResponse.ResponseSuccess && initialPasswordsList is UserPasswordsResponse.ResponseSuccess) {

            Truth.assertThat(initialPasswordsList.passwords.size).isEqualTo(3)
            Truth.assertThat(currentPasswordsList.passwords.size).isEqualTo(2)
        }
    }

}