package com.thejohnsondev.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thejohnsondev.common.getAuthErrorMessage
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.HttpError
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.NetworkError
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.model.UnknownApiError
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

    protected fun handleError(error: ApiError) {
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