package com.thejohnsondev.presentation

import com.thejohnsondev.common.base.BaseViewModel
import com.thejohnsondev.domain.SettingsUseCases
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.model.settings.SettingsConfig
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
    private val _openConfirmDeleteAccountDialog = MutableStateFlow(false)
    private val _openConfirmLogoutDialog = MutableStateFlow(false)
    private val _settingsConfig = useCases.getSettingsConfig()

    val viewState = combine(
        _loadingState,
        _userEmail,
        _openConfirmDeleteAccountDialog,
        _openConfirmLogoutDialog,
        _settingsConfig,
        ::State,
    )

    fun perform(action: Action) {
        when (action) {
            is Action.FetchData -> fetchData()
            is Action.Logout -> logout()
            is Action.DeleteAccount -> deleteAccount()
            is Action.CloseConfirmDeleteAccountDialog -> closeConfirmDeleteAccountDialog()
            is Action.CloseConfirmLogoutDialog -> closeConfirmLogoutDialog()
            is Action.OpenConfirmDeleteAccountDialog -> openConfirmDeleteAccountDialog()
            is Action.OpenConfirmLogoutDialog -> openConfirmLogoutDialog()
        }
    }

    private fun openConfirmDeleteAccountDialog() {
        _openConfirmDeleteAccountDialog.value = true
    }

    private fun closeConfirmDeleteAccountDialog() {
        _openConfirmDeleteAccountDialog.value = false
    }

    private fun openConfirmLogoutDialog() {
        _openConfirmLogoutDialog.value = true
    }

    private fun closeConfirmLogoutDialog() {
        _openConfirmLogoutDialog.value = false
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
        sendEvent(OneTimeEvent.SuccessNavigation())
    }

    sealed class Action {
        object FetchData : Action()
        object Logout : Action()
        object DeleteAccount : Action()
        object OpenConfirmDeleteAccountDialog : Action()
        object CloseConfirmDeleteAccountDialog : Action()
        object OpenConfirmLogoutDialog : Action()
        object CloseConfirmLogoutDialog : Action()
    }

    data class State(
        val loadingState: LoadingState = LoadingState.Loaded,
        val userEmail: String? = null,
        val openConfirmDeleteAccountDialog: Boolean = false,
        val openConfirmLogoutDialog: Boolean = false,
        val settingsConfig: SettingsConfig? = null
    )

}