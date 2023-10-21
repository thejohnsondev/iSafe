package com.thejohnsondev.isafe.presentation.screens.auth.login

import com.thejohnsondev.isafe.domain.models.AuthResponse
import com.thejohnsondev.isafe.domain.models.EmailValidationState
import com.thejohnsondev.isafe.domain.models.LoadingState
import com.thejohnsondev.isafe.domain.models.OneTimeEvent
import com.thejohnsondev.isafe.domain.models.PasswordValidationState
import com.thejohnsondev.isafe.domain.models.UserDataResponse
import com.thejohnsondev.isafe.domain.models.UserModel
import com.thejohnsondev.isafe.domain.use_cases.combined.AuthUseCases
import com.thejohnsondev.isafe.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
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
        useCases.signIn(email, password).collect {
            when (it) {
                is AuthResponse.ResponseFailure -> {
                    handleError(it.exception)
                }

                is AuthResponse.ResponseSuccess -> {
                    getUserData(it.authResult.user?.uid.orEmpty())
                }
            }
        }
    }

    private fun getUserData(userId: String) = launch {
        useCases.getUserData(userId).collect {
            when (it) {
                is UserDataResponse.ResponseFailure -> handleError(it.exception)
                is UserDataResponse.ResponseSuccess -> saveUserData(it.userModel)
            }
        }
    }

    private fun saveUserData(userModel: UserModel) = launch {
        useCases.saveUserData(userModel, true)
        sendEvent(OneTimeEvent.SuccessNavigation)
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