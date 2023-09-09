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

    val viewState: Flow<EnterEncryptionKeyViewState> = combine(
        _loadingState,
        _keyGenerationState,
        _isUploadedKeyFileCorrect,
        ::mergeSources
    )

    fun perform(action: EnterEncryptionKeyAction) {
        when (action) {
            is EnterEncryptionKeyAction.GenerateKey -> generateKey(action.fileUri)
            is EnterEncryptionKeyAction.Logout -> logout()
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
    ): EnterEncryptionKeyViewState = EnterEncryptionKeyViewState(
        loadingState = loadingState,
        keyGenerationState = keyGenerationState,
        isUploadedKeyFileCorrect = isUploadedKeyFileCorrect
    )

}