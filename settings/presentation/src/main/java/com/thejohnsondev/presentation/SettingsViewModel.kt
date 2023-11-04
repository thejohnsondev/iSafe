package com.thejohnsondev.presentation

import com.thejohnsondev.common.base.BaseViewModel
import com.thejohnsondev.domain.SettingsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val useCases: SettingsUseCases
) : BaseViewModel() {

    fun perform(action: Action) {
        when (action) {
            is Action.Logout -> logout()
        }
    }

    private fun logout() = launch {
        useCases.logout.invoke()
    }

    sealed class Action {
        object Logout : Action()
    }

}