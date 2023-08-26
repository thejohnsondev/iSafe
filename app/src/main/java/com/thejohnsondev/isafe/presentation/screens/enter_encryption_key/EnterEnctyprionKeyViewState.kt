package com.thejohnsondev.isafe.presentation.screens.enter_encryption_key

import com.thejohnsondev.isafe.domain.models.KeyGenerationState
import com.thejohnsondev.isafe.domain.models.LoadingState

data class EnterEncryptionKeyViewState(
    val loadingState: LoadingState = LoadingState.Loaded,
    val keyGenerationState: KeyGenerationState = KeyGenerationState.NOT_GENERATED,
    val isUploadedKeyFileCorrect: Boolean? = null
)