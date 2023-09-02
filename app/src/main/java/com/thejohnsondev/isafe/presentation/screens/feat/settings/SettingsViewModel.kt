package com.thejohnsondev.isafe.presentation.screens.feat.settings

import com.thejohnsondev.isafe.domain.use_cases.combined.SettingsUseCases
import com.thejohnsondev.isafe.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val useCases: SettingsUseCases
) : BaseViewModel() {

    fun perform(action: SettingsAction) {
        when (action) {
            is SettingsAction.Logout -> logout()
        }
    }

    private fun logout() = launch {
        useCases.logout()
    }

}