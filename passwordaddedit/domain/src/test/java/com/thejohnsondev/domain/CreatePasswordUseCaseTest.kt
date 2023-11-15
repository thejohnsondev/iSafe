package com.thejohnsondev.domain

import com.google.common.truth.Truth
import com.thejohnsondev.data.PasswordsRepository
import com.thejohnsondev.data.test.FakePasswordRepository
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.PasswordModel
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
class CreatePasswordUseCaseTest {

    private lateinit var passwordsRepository: PasswordsRepository
    private lateinit var createPasswordUseCase: CreatePasswordUseCase

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Before
    fun setUp() {
        passwordsRepository = FakePasswordRepository()
        createPasswordUseCase = CreatePasswordUseCaseImpl(passwordsRepository)
    }

    @Test
    fun givenValidInput_returnsSuccess() = runBlocking {
        val password =
            PasswordModel(id = "14", organization = "org", title = "title", password = "pass")

        val result = createPasswordUseCase("1", password).first()

        Truth.assertThat(result).isInstanceOf(DatabaseResponse.ResponseSuccess::class.java)
    }

    @Test
    fun givenValidInput_savesPassword() = runBlocking {
        val password =
            PasswordModel(id = "14", organization = "org14", title = "title14", password = "pass14")
        val passwordsBefore = (passwordsRepository as FakePasswordRepository).passwords["1"]

        createPasswordUseCase("1", password).first()
        val passwordsAfter = (passwordsRepository as FakePasswordRepository).passwords["1"]

        Truth.assertThat(passwordsBefore).doesNotContain(password)
        Truth.assertThat(passwordsAfter).contains(password)
    }

}