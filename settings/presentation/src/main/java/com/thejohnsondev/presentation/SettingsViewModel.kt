package com.thejohnsondev.presentation

import com.thejohnsondev.common.base.BaseViewModel
import com.thejohnsondev.domain.SettingsUseCases
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.OneTimeEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val useCases: SettingsUseCases
) : BaseViewModel() {

    private val _userEmail = MutableStateFlow<String?>(null)

    val viewState = combine(
        _loadingState,
        _userEmail,
        ::State,
    )

    fun perform(action: Action) {
        when (action) {
            is Action.FetchData -> fetchData()
            is Action.Logout -> logout()
            is Action.DeleteAccount -> deleteAccount()
        }
    }

    private fun fetchData() = launch {
        _userEmail.value = useCases.getUserEmail.invoke()
    }

    private fun logout() = launch {
        useCases.logout.invoke()
    }

    private fun deleteAccount() = launch {
        useCases.deleteAccount.invoke().first().fold(
            ifLeft = ::handleError,
            ifRight = {
                handleSuccess()
            }
        )
    }

    private fun handleSuccess() = launch {
        sendEvent(OneTimeEvent.SuccessNavigation)
    }

    sealed class Action {
        object FetchData : Action()
        object Logout : Action()
        object DeleteAccount : Action()
    }

    data class State(
        val loadingState: LoadingState = LoadingState.Loaded,
        val userEmail: String? = null
    )

}