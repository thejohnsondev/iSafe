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

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Loaded)
    private val _keyGenerationState = MutableStateFlow(KeyGenerationState.NOT_GENERATED)

    val viewState: Flow<CreateEncryptionKeyViewState> = combine(
        _loadingState,
        _keyGenerationState,
        ::mergeSources
    )

    fun perform(action: CreateEncryptionKeyAction) {
        when (action) {
            is CreateEncryptionKeyAction.GenerateKey -> generateKey(action.fileUri)
            is CreateEncryptionKeyAction.Logout -> logout()
        }
    }

    private fun logout() = launch {
        useCases.logout()
    }

    private fun generateKey(fileUri: Uri?) = launch {
        _loadingState.value = LoadingState.Loading
        useCases.generateUserKey(fileUri)
            .flowOn(Dispatchers.Default)
            .collect {
                when (it) {
                    is KeyGenerateResult.Failure -> {
                        _loadingState.value = LoadingState.Loaded
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
                        _loadingState.value = LoadingState.Loaded
                        handleError(it.exception)
                    }

                    DatabaseResponse.ResponseSuccess -> {
                        _loadingState.value = LoadingState.Loaded
                        sendEvent(OneTimeEvent.SuccessNavigation)
                    }
                }
            }
    }

    private fun mergeSources(
        loadingState: LoadingState,
        keyGenerationState: KeyGenerationState
    ): CreateEncryptionKeyViewState = CreateEncryptionKeyViewState(
        loadingState = loadingState,
        keyGenerationState = keyGenerationState
    )
}