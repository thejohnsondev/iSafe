package com.thejohnsondev.isafe.utils.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thejohnsondev.isafe.domain.models.ApiError
import com.thejohnsondev.isafe.domain.models.HttpError
import com.thejohnsondev.isafe.domain.models.LoadingState
import com.thejohnsondev.isafe.domain.models.NetworkError
import com.thejohnsondev.isafe.domain.models.OneTimeEvent
import com.thejohnsondev.isafe.domain.models.UnknownApiError
import com.thejohnsondev.isafe.utils.getAuthErrorMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val eventChannel = Channel<OneTimeEvent>()
    protected val _loadingState: MutableStateFlow<LoadingState> =
        MutableStateFlow(LoadingState.Loaded)

    fun getEventFlow() = eventChannel.receiveAsFlow()


    protected fun BaseViewModel.sendEvent(event: OneTimeEvent) = launch {
        loaded()
        eventChannel.send(event)
    }

    protected fun BaseViewModel.loading() = launch {
        _loadingState.emit(LoadingState.Loading)
    }

    protected fun BaseViewModel.loaded() = launch {
        _loadingState.emit(LoadingState.Loaded)
    }

    protected fun BaseViewModel.handleError(error: ApiError) {
        loaded()
        val errorMessage = when (error) {
            is HttpError -> error.message
            is NetworkError -> "Please, check your internet connection"
            is UnknownApiError -> error.throwable.message
        }
        sendEvent(OneTimeEvent.InfoToast(getAuthErrorMessage(errorMessage)))
    }

    protected fun BaseViewModel.handleError(error: Exception?) {
        loaded()
        sendEvent(OneTimeEvent.InfoSnackbar(getAuthErrorMessage(error?.message)))
    }

    protected fun BaseViewModel.launch(block: suspend CoroutineScope.() -> Unit): Job {
        val job = viewModelScope.launch {
            block.invoke(viewModelScope)
        }
        return job
    }

    protected fun BaseViewModel.launchLoading(block: suspend CoroutineScope.() -> Unit): Job {
        val job = viewModelScope.launch {
            loading()
            block.invoke(viewModelScope)
        }
        return job
    }

}