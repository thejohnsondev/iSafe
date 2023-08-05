package com.thejohnsondev.isafe.domain.models

sealed class LoadingState {
    object Loading: LoadingState()
    object Loaded: LoadingState()
}