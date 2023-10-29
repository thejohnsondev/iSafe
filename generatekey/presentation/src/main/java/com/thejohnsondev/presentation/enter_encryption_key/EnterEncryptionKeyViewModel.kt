package com.thejohnsondev.isafe.presentation.screens.auth.enter_encryption_key

import android.net.Uri
import com.thejohnsondev.isafe.domain.models.KeyGenerateResult
import com.thejohnsondev.isafe.domain.models.KeyGenerationState
import com.thejohnsondev.isafe.domain.models.LoadingState
import com.thejohnsondev.isafe.domain.models.OneTimeEvent
import com.thejohnsondev.isafe.domain.use_cases.combined.EnterEncryptionKeyUseCases
import com.thejohnsondev.isafe.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class EnterEncryptionKeyViewModel @Inject constructor(
    private val useCases: EnterEncryptionKeyUseCases
) : BaseViewModel() {

    private val _keyGenerationState = MutableStateFlow(KeyGenerationState.NOT_GENERATED)
    private val _isUploadedKeyFileCorrect = MutableStateFlow<Boolean?>(null)

    val viewState: Flow<State> = combine(
        _loadingState,
        _keyGenerationState,
        _isUploadedKeyFileCorrect,
        ::mergeSources
    )

    fun perform(action: Action) {
        when (action) {
            is Action.GenerateKey -> generateKey(action.fileUri)
            is Action.Logout -> logout()
        }
    }

    private fun logout() = launch{
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
                        checkIsKeyCorrect(it.key)
                    }
                }
            }
    }

    private fun checkIsKeyCorrect(key: ByteArray) = launch {
        useCases.checkUserKeyCorrect(key).collect { isFileCorrect ->
            _isUploadedKeyFileCorrect.emit(isFileCorrect)
            loaded()
            if (isFileCorrect) {
                useCases.saveUserKey(key)
                sendEvent(OneTimeEvent.SuccessNavigation)
            }
        }
    }

    private fun mergeSources(
        loadingState: LoadingState,
        keyGenerationState: KeyGenerationState,
        isUploadedKeyFileCorrect: Boolean?
    ): State = State(
        loadingState = loadingState,
        keyGenerationState = keyGenerationState,
        isUploadedKeyFileCorrect = isUploadedKeyFileCorrect
    )

    sealed class Action {
        class GenerateKey(val fileUri: Uri?) : Action()
        object Logout : Action()
    }

    data class State(
        val loadingState: LoadingState = LoadingState.Loaded,
        val keyGenerationState: KeyGenerationState = KeyGenerationState.NOT_GENERATED,
        val isUploadedKeyFileCorrect: Boolean? = null
    )

}