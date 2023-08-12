package com.thejohnsondev.isafe.utils.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thejohnsondev.isafe.domain.models.ApiError
import com.thejohnsondev.isafe.domain.models.HttpError
import com.thejohnsondev.isafe.domain.models.NetworkError
import com.thejohnsondev.isafe.domain.models.OneTimeEvent
import com.thejohnsondev.isafe.domain.models.UnknownApiError
import com.thejohnsondev.isafe.utils.getAuthErrorMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val eventChannel = Channel<OneTimeEvent>()

    fun getEventFlow() = eventChannel.receiveAsFlow()

    fun sendEvent(event: OneTimeEvent) = launch {
        eventChannel.send(event)
    }

    fun handleError(error: ApiError) {
        val errorMessage = when (error) {
            is HttpError -> error.message
            is NetworkError -> "Please, check your internet connection"
            is UnknownApiError -> error.throwable.message
        }
        sendEvent(OneTimeEvent.InfoToast(getAuthErrorMessage(errorMessage)))
    }

    fun handleError(error: Exception?) {
        sendEvent(OneTimeEvent.InfoSnackbar(getAuthErrorMessage(error?.message)))
    }

    fun ViewModel.launch(block: suspend CoroutineScope.() -> Unit): Job {
        val job = viewModelScope.launch {
            block.invoke(viewModelScope)
        }
        return job
    }

}