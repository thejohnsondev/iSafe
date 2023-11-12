package com.thejohnsondev.domain

import com.google.common.truth.Truth
import com.thejohnsondev.data.PasswordsRepository
import com.thejohnsondev.model.DatabaseResponse
import kotlinx.coroutines.flow.flowOf
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

    @Mock
    private lateinit var passwordsRepository: PasswordsRepository
    private lateinit var deletePasswordsUseCase: DeletePasswordUseCaseImpl

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Before
    fun setUp() {
        deletePasswordsUseCase = DeletePasswordUseCaseImpl(passwordsRepository)
    }

    @Test
    fun givenValidUserAndPasswordId_returnsSuccess() = runBlocking {
        val correctFlowToReturn = flowOf(DatabaseResponse.ResponseSuccess)
        given(passwordsRepository.deletePassword("userId", "id")).willReturn(correctFlowToReturn)

        val result = deletePasswordsUseCase("userId", "id")

        Truth.assertThat(result).isEqualTo(correctFlowToReturn)
    }

}