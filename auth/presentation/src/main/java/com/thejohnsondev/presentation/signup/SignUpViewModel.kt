package com.thejohnsondev.presentation.signup

import com.thejohnsondev.common.base.BaseViewModel
import com.thejohnsondev.domain.AuthUseCases
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.EmailValidationState
import com.thejohnsondev.model.KeyGenerateResult
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.model.PasswordValidationState
import com.thejohnsondev.model.UserModel
import com.thejohnsondev.model.auth.AuthResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val useCases: AuthUseCases
) : BaseViewModel() {

    private val _isSignUpSuccess = MutableStateFlow<Boolean?>(null)
    private val _emailValidationState = MutableStateFlow<EmailValidationState?>(null)
    private val _passwordValidationState = MutableStateFlow<PasswordValidationState?>(null)
    private val _signUpReadyState: Flow<Boolean> = combine(
        _emailValidationState,
        _passwordValidationState,
        ::isSignUpReady
    )

    val viewState: Flow<State> = combine(
        _isSignUpSuccess,
        _loadingState,
        _emailValidationState,
        _passwordValidationState,
        _signUpReadyState,
        ::mergeSources
    )

    fun perform(action: Action) {
        when (action) {
            is Action.SignUpWithEmail -> signUp(action.email, action.password)
            is Action.ValidateEmail -> validateEmail(action.email)
            is Action.ValidatePassword -> validatePassword(action.password)
        }
    }

    private fun validateEmail(email: String) = launch {
        _emailValidationState.value = useCases.validateEmail(email)
    }

    private fun validatePassword(password: String) = launch {
        _passwordValidationState.value = useCases.validatePassword(password)
    }

    private fun signUp(email: String, password: String) = launchLoading {
        useCases.signUp(email, password).first()
            .fold(
                ifLeft = ::handleError,
                ifRight = {
                    handleAuthResponse(it, password)
                }
            )
    }

    private fun handleAuthResponse(authResponse: AuthResponse, password: String) = launch {
        saveUserToken(authResponse.token)
        generateAndSaveEncryptionKey(password)
        sendEvent(OneTimeEvent.SuccessNavigation)
    }

    private fun saveUserToken(token: String) = launch {
        useCases.saveUserToken.invoke(token)
    }

    private suspend fun generateAndSaveEncryptionKey(password: String) {
        useCases.generateUserKey(password).collect {
            when (it) {
                is KeyGenerateResult.Failure -> handleError(it.exception)
                is KeyGenerateResult.Success -> handleGenerateKeySuccess(it.key)
            }
        }
    }

    private suspend fun handleGenerateKeySuccess(generatedKey: ByteArray) {
        useCases.saveUserKey(generatedKey)
    }


    private fun mergeSources(
        isSignUpSuccess: Boolean?,
        authLoadingState: LoadingState,
        emailValidationState: EmailValidationState?,
        passwordValidationState: PasswordValidationState?,
        signUpReady: Boolean
    ): State = State(
        isSignUpSuccess = isSignUpSuccess,
        loadingState = authLoadingState,
        emailValidationState = emailValidationState,
        passwordValidationState = passwordValidationState,
        signUpReady = signUpReady
    )


    private fun isSignUpReady(
        emailValidationState: EmailValidationState?,
        passwordValidationState: PasswordValidationState?
    ): Boolean = emailValidationState is EmailValidationState.EmailCorrectState
            && passwordValidationState is PasswordValidationState.PasswordCorrectState

    sealed class Action {
        class SignUpWithEmail(
            val email: String,
            val password: String
        ) : Action()

        class ValidateEmail(val email: String) : Action()
        class ValidatePassword(val password: String) : Action()
    }

    data class State(
        val isSignUpSuccess: Boolean? = null,
        val loadingState: LoadingState = LoadingState.Loaded,
        val emailValidationState: EmailValidationState? = null,
        val passwordValidationState: PasswordValidationState? = null,
        val signUpReady: Boolean = false
    )
}