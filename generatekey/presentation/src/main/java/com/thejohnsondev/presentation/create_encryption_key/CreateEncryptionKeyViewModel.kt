package com.thejohnsondev.isafe.presentation.screens.auth.create_encryption_key

import android.net.Uri
import com.thejohnsondev.isafe.domain.models.DatabaseResponse
import com.thejohnsondev.isafe.domain.models.KeyGenerateResult
import com.thejohnsondev.isafe.domain.models.KeyGenerationState
import com.thejohnsondev.isafe.domain.models.LoadingState
import com.thejohnsondev.isafe.domain.models.OneTimeEvent
import com.thejohnsondev.isafe.domain.use_cases.combined.CreateKeyUseCases
import com.thejohnsondev.isafe.utils.base.BaseViewModel
import com.thejohnsondev.isafe.utils.encrypt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class CreateEncryptionKeyViewModel @Inject constructor(
    private val useCases: CreateKeyUseCases
) : BaseViewModel() {

    private val _keyGenerationState = MutableStateFlow(KeyGenerationState.NOT_GENERATED)

    val viewState: Flow<State> = combine(
        _loadingState,
        _keyGenerationState,
        ::mergeSources
    )

    fun perform(action: Action) {
        when (action) {
            is Action.GenerateKey -> generateKey(action.fileUri)
            is Action.Logout -> logout()
        }
    }

    private fun logout() = launch {
        useCases.logout()
    }

    private fun generateKey(fileUri: Uri?) = launchLoading {
        useCases.generateUserKey(fileUri)
            .flowOn(Dispatchers.Default)
            .collect {
                when (it) {
                    is KeyGenerateResult.Failure -> {
                        handleError(it.exception)
                    }

                    is KeyGenerateResult.Success -> {
                        handleKeyGenerationSuccess(it.key)
                    }
                }
            }
    }

    private fun handleKeyGenerationSuccess(key: ByteArray) = launch {
        val userId = useCases.getLocalUserData().id ?: return@launch
        useCases.saveUserKey(key)
        useCases.saveUserSecret(userId, userId.encrypt(key))
            .collect {
                when (it) {
                    is DatabaseResponse.ResponseFailure -> {
                        handleError(it.exception)
                    }

                    DatabaseResponse.ResponseSuccess -> {
                        sendEvent(OneTimeEvent.SuccessNavigation)
                    }
                }
            }
    }

    private fun mergeSources(
        loadingState: LoadingState,
        keyGenerationState: KeyGenerationState
    ): State = State(
        loadingState = loadingState,
        keyGenerationState = keyGenerationState
    )

    sealed class Action {
        class GenerateKey(val fileUri: Uri?) : Action()
        object Logout : Action()
    }

    data class State(
        val loadingState: LoadingState = LoadingState.Loaded,
        val keyGenerationState: KeyGenerationState = KeyGenerationState.NOT_GENERATED
    )
}