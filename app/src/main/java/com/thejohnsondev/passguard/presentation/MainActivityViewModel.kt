package com.thejohnsondev.passguard.presentation

import com.thejohnsondev.common.base.BaseViewModel
import com.thejohnsondev.domain.GetSettingsConfigFlowUseCase
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.settings.SettingsConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getSettingsConfig: GetSettingsConfigFlowUseCase
) : BaseViewModel() {

    private val _settingsConfig = getSettingsConfig()
    val state = combine(
        _loadingState,
        _settingsConfig,
        ::State
    )

    data class State(
        val loadingState: LoadingState = LoadingState.Loaded,
        val settingsConfig: SettingsConfig? = null
    )

}