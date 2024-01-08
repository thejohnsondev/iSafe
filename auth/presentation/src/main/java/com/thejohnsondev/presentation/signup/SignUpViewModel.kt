package com.thejohnsondev.presentation.signup

import android.util.Log
import com.thejohnsondev.common.EMPTY
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
        _emailValidationState.value = EmailValidationState.EmailCorrectState

    }

    private fun validatePassword(password: String) = launch {
        _passwordValidationState.value = useCases.validatePassword(password)
        _passwordValidationState.value = PasswordValidationState.PasswordCorrectState
    }

    private fun signUp(name: String, email: String, password: String) = launchLoading {
        useCases.signUp(email, password).first()
            .fold(
                ifLeft = ::handleError,
                ifRight = ::handleAuthResponse
            )
    }

    private fun handleSignUpSuccess(userModel: UserModel, password: String) = launch {
        saveUserData(userModel, password)
        sendEvent(OneTimeEvent.SuccessNavigation)
    }

    private fun handleAuthResponse(authResponse: AuthResponse) {
        Log.e("TAG", "-- register response: ${authResponse.token}")
        sendEvent(OneTimeEvent.InfoSnackbar("Token: ${authResponse.token}"))
    }

    private fun createUserInRemoteDb(userUID: String, userName: String, password: String) = launch {
        val newUserModel = UserModel(
            id = userUID,
            name = userName,
            userSecret = null
        )
        useCases.createUser(newUserModel).collect {
            when (it) {
                is DatabaseResponse.ResponseSuccess -> handleSignUpSuccess(newUserModel, password)
                is DatabaseResponse.ResponseFailure -> handleError(it.exception)
            }
        }
    }

    private suspend fun saveUserData(userModel: UserModel, password: String) = launch {
        useCases.saveUserData(userModel, false)
        generateAndSaveEncryptionKey(password)
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