package com.thejohnsondev.isafe.presentation.screens.create_encryption_key

import com.thejohnsondev.isafe.domain.models.KeyGenerationState
import com.thejohnsondev.isafe.domain.models.LoadingState

data class CreateEncryptionKeyViewState(
    val loadingState: LoadingState = LoadingState.Loaded,
    val keyGenerationState: KeyGenerationState = KeyGenerationState.NOT_GENERATED
)