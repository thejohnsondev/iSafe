package com.thejohnsondev.domain

import com.google.common.truth.Truth.assertThat
import com.thejohnsondev.data.PasswordsRepository
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.model.UserPasswordsResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
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
class GetAllPasswordsUseCaseTest {

    @Mock
    private lateinit var passwordsRepository: PasswordsRepository
    private lateinit var getAllPasswordsUseCase: GetAllPasswordsUseCaseImpl

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Before
    fun setUp() {
        getAllPasswordsUseCase = GetAllPasswordsUseCaseImpl(passwordsRepository)
    }

    @Test
    fun givenValidUserId_returnsSuccess() = runBlocking {
        val correctFlowToReturn = flowOf(UserPasswordsResponse.ResponseSuccess(emptyList()))
        given(passwordsRepository.getUserPasswords("id")).willReturn(correctFlowToReturn)

        val result = getAllPasswordsUseCase("id")

        assertThat(result).isEqualTo(correctFlowToReturn)
    }

    @Test
    fun givenValidUserId_returnsValidList() = runBlocking {
        val correctFlowToReturn = flowOf(
            UserPasswordsResponse.ResponseSuccess(
                listOf(
                    PasswordModel(
                        id = "id",
                        organization = "org",
                        title = "title",
                        password = "pass"
                    )
                )
            )
        )
        given(passwordsRepository.getUserPasswords("id")).willReturn(correctFlowToReturn)

        val result = getAllPasswordsUseCase("id").first()

        assertThat(result).isInstanceOf(UserPasswordsResponse.ResponseSuccess::class.java)
        if (result is UserPasswordsResponse.ResponseSuccess) {
            assertThat(result.passwords.first().id == "id"
                    && result.passwords.first().organization == "org"
                    && result.passwords.first().title == "title"
                    && result.passwords.first().password == "pass")
        }
    }

}