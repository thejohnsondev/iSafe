package com.thejohnsondev.isafe.presentation.screens.settings

import com.thejohnsondev.isafe.domain.use_cases.combined.SettingsUseCases
import com.thejohnsondev.isafe.utils.base.BaseViewModel
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
        useCases.logout()
    }

    sealed class Action {
        object Logout : Action()
    }

}