package com.thejohnsondev.presentation.login

import android.util.Log
import com.thejohnsondev.common.base.BaseViewModel
import com.thejohnsondev.domain.AuthUseCases
import com.thejohnsondev.model.EmailValidationState
import com.thejohnsondev.model.KeyGenerateResult
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.model.PasswordValidationState
import com.thejohnsondev.model.UserDataResponse
import com.thejohnsondev.model.UserModel
import com.thejohnsondev.model.auth.AuthResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val useCases: AuthUseCases
) : BaseViewModel() {

    private val _isLoginSuccess = MutableStateFlow<Boolean?>(null)
    private val _emailValidationState = MutableStateFlow<EmailValidationState?>(null)
    private val _passwordValidationState = MutableStateFlow<PasswordValidationState?>(null)
    private val _loginReadyState: Flow<Boolean> = combine(
        _emailValidationState,
        _passwordValidationState,
        ::isLoginReady
    )

    val viewState: Flow<State> = combine(
        _isLoginSuccess,
        _loadingState,
        _emailValidationState,
        _passwordValidationState,
        _loginReadyState,
        ::mergeSources
    )

    fun perform(action: Action) {
        when (action) {
            is Action.LoginWithEmail -> loginWithEmail(action.email, action.password)
            is Action.ValidateEmail -> validateEmail(action.email)
            is Action.ValidatePassword -> validatePassword(action.password)
        }
    }

    private fun loginWithEmail(email: String, password: String) = launchLoading {
        useCases.signIn(email, password).first()
            .fold(
                ifLeft = ::handleError,
                ifRight = ::handleAuthResponse
            )
    }

    private fun handleAuthResponse(authResponse: AuthResponse) {
        Log.e("TAG", "-- login response: ${authResponse.token}")
        sendEvent(OneTimeEvent.InfoSnackbar("Token: ${authResponse.token}"))
    }

    private fun getUserData(userId: String, password: String) = launch {
        useCases.getUserData(userId).collect {
            when (it) {
                is UserDataResponse.ResponseFailure -> handleError(it.exception)
                is UserDataResponse.ResponseSuccess -> saveUserData(it.userModel, password)
            }
        }
    }

    private fun saveUserData(userModel: UserModel, password: String) = launch {
        useCases.saveUserData.invoke(userModel, true)
        generateAndSaveEncryptionKey(password)
        sendEvent(OneTimeEvent.SuccessNavigation)
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

    private fun validateEmail(email: String) = launch {
        _emailValidationState.value = useCases.validateEmail(email)
    }


    private fun validatePassword(password: String) = launch {
        _passwordValidationState.value = useCases.validatePassword(password)
    }


    private fun mergeSources(
        isLoginSuccess: Boolean?,
        authLoadingState: LoadingState,
        emailValidationState: EmailValidationState?,
        passwordValidationState: PasswordValidationState?,
        loginReady: Boolean
    ): State = State(
        isLoginSuccess = isLoginSuccess,
        loadingState = authLoadingState,
        emailValidationState = emailValidationState,
        passwordValidationState = passwordValidationState,
        loginReady = loginReady
    )


    private fun isLoginReady(
        emailValidationState: EmailValidationState?,
        passwordValidationState: PasswordValidationState?
    ): Boolean = emailValidationState is EmailValidationState.EmailCorrectState
            && passwordValidationState is PasswordValidationState.PasswordCorrectState


    sealed class Action {
        class LoginWithEmail(val email: String, val password: String) : Action()
        class ValidateEmail(val email: String) : Action()
        class ValidatePassword(val password: String) : Action()
    }

    data class State(
        val isLoginSuccess: Boolean? = null,
        val loadingState: LoadingState = LoadingState.Loaded,
        val emailValidationState: EmailValidationState? = null,
        val passwordValidationState: PasswordValidationState? = null,
        val loginReady: Boolean = false
    )
}