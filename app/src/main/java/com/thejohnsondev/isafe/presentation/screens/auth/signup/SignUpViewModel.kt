package com.thejohnsondev.isafe.presentation.screens.auth.signup

import com.thejohnsondev.isafe.domain.models.AuthResponse
import com.thejohnsondev.isafe.domain.models.DatabaseResponse
import com.thejohnsondev.isafe.domain.models.EmailValidationState
import com.thejohnsondev.isafe.domain.models.LoadingState
import com.thejohnsondev.isafe.domain.models.OneTimeEvent
import com.thejohnsondev.isafe.domain.models.PasswordValidationState
import com.thejohnsondev.isafe.domain.models.UserModel
import com.thejohnsondev.isafe.domain.use_cases.combined.AuthUseCases
import com.thejohnsondev.isafe.utils.EMPTY
import com.thejohnsondev.isafe.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val useCases: AuthUseCases
) : BaseViewModel() {

    private val _isSignUpSuccess = MutableStateFlow<Boolean?>(null)
    private val _nameState = MutableStateFlow(EMPTY)
    private val _emailValidationState = MutableStateFlow<EmailValidationState?>(null)
    private val _passwordValidationState = MutableStateFlow<PasswordValidationState?>(null)
    private val _signUpReadyState: Flow<Boolean> = combine(
        _nameState,
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
            is Action.SignUpWithEmail -> signUp(action.name, action.email, action.password)
            is Action.ValidateEmail -> validateEmail(action.email)
            is Action.ValidatePassword -> validatePassword(action.password)
            is Action.EnterName -> enterName(action.name)
        }
    }

    private fun enterName(name: String) = launch {
        _nameState.value = name
    }

    private fun validateEmail(email: String) = launch {
        _emailValidationState.value = useCases.validateEmail(email)

    }

    private fun validatePassword(password: String) = launch {
        _passwordValidationState.value = useCases.validatePassword(password)
    }

    private fun signUp(name: String, email: String, password: String) = launchLoading {
        useCases.signUp(email, password).collect {
            when (it) {
                is AuthResponse.ResponseSuccess -> {
                    createUserInRemoteDb(it.authResult.user?.uid.toString(), name)
                }

                is AuthResponse.ResponseFailure -> {
                    handleError(it.exception)
                }
            }
        }
    }

    private fun handleSignUpSuccess(userModel: UserModel) = launch {
        saveUserData(userModel)
        sendEvent(OneTimeEvent.SuccessNavigation)
    }

    private fun createUserInRemoteDb(userUID: String, userName: String) = launch {
        val newUserModel = UserModel(
            id = userUID,
            name = userName,
            userSecret = null
        )
        useCases.createUser(newUserModel).collect {
            when (it) {
                is DatabaseResponse.ResponseSuccess -> handleSignUpSuccess(newUserModel)
                is DatabaseResponse.ResponseFailure -> handleError(it.exception)
            }
        }
    }

    private suspend fun saveUserData(userModel: UserModel) = launch {
        useCases.saveUserData(userModel, false)
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
        name: String,
        emailValidationState: EmailValidationState?,
        passwordValidationState: PasswordValidationState?
    ): Boolean =
        name.isNotBlank()
                && emailValidationState is EmailValidationState.EmailCorrectState
                && passwordValidationState is PasswordValidationState.PasswordCorrectState

    sealed class Action {
        class SignUpWithEmail(
            val name: String,
            val email: String,
            val password: String
        ) : Action()

        class ValidateEmail(val email: String) : Action()
        class ValidatePassword(val password: String) : Action()
        class EnterName(val name: String) : Action()
    }

    data class State(
        val isSignUpSuccess: Boolean? = null,
        val loadingState: LoadingState = LoadingState.Loaded,
        val emailValidationState: EmailValidationState? = null,
        val passwordValidationState: PasswordValidationState? = null,
        val signUpReady: Boolean = false
    )
}