package com.thejohnsondev.model

sealed class LoadingState {
    object Loading: LoadingState()
    object Loaded: LoadingState()
}