package com.thejohnsondev.domain

import com.google.common.truth.Truth.assertThat
import com.thejohnsondev.data.PasswordsRepository
import com.thejohnsondev.data.test.FakePasswordRepository
import com.thejohnsondev.model.UserPasswordsResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule

@RunWith(MockitoJUnitRunner::class)
class GetAllPasswordsUseCaseTest {

    private lateinit var passwordsRepository: PasswordsRepository
    private lateinit var getAllPasswordsUseCase: GetAllPasswordsUseCaseImpl

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Before
    fun setUp() {
        passwordsRepository = FakePasswordRepository()
        getAllPasswordsUseCase = GetAllPasswordsUseCaseImpl(passwordsRepository)
    }

    @Test
    fun givenValidUserId_returnsSuccess() = runBlocking {
        val result = getAllPasswordsUseCase("1").first()

        assertThat(result).isInstanceOf(UserPasswordsResponse.ResponseSuccess::class.java)
    }

    @Test
    fun givenValidUserId_returnsValidList() = runBlocking {
        val result = getAllPasswordsUseCase("1").first()

        if (result is UserPasswordsResponse.ResponseSuccess) {
            assertThat(result.passwords.first()).isEqualTo(FakePasswordRepository.user1TestPasswords.first())
            assertThat(result.passwords.last()).isEqualTo(FakePasswordRepository.user1TestPasswords.last())
        }
    }

}