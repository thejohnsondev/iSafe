package com.thejohnsondev.presentation

import androidx.lifecycle.viewModelScope
import com.thejohnsondev.common.base.BaseViewModel
import com.thejohnsondev.common.combine
import com.thejohnsondev.domain.SettingsUseCases
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.model.settings.DarkThemeConfig
import com.thejohnsondev.model.settings.GeneralSettings
import com.thejohnsondev.model.settings.PrivacySettings
import com.thejohnsondev.model.settings.SettingsConfig
import com.thejohnsondev.model.settings.ThemeBrand
import com.thejohnsondev.ui.ui_model.SettingsSection
import com.thejohnsondev.ui.ui_model.SettingsSubSection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val useCases: SettingsUseCases
) : BaseViewModel() {

    private val _settingsSections = flowOf(SettingsSection.getSettingsSections())
    private val _userEmail = MutableStateFlow<String?>(null)
    private val _openConfirmDeleteAccountDialog = MutableStateFlow(false)
    private val _openConfirmLogoutDialog = MutableStateFlow(false)
    private val _settingsConfig = MutableStateFlow<SettingsConfig?>(null)

    init {
        viewModelScope.launch {
            _settingsConfig.emit(useCases.getSettingsConfig().first())
        }
    }

    val viewState = combine(
        _loadingState,
        _settingsSections,
        _userEmail,
        _openConfirmDeleteAccountDialog,
        _openConfirmLogoutDialog,
        _settingsConfig,
        ::State
    ).stateIn(viewModelScope, SharingStarted.Eagerly, State())

    fun perform(action: Action) {
        when (action) {
            is Action.FetchData -> fetchData()
            is Action.Logout -> logout()
            is Action.DeleteAccount -> deleteAccount()
            is Action.CloseConfirmDeleteAccountDialog -> closeConfirmDeleteAccountDialog()
            is Action.CloseConfirmLogoutDialog -> closeConfirmLogoutDialog()
            is Action.OpenConfirmDeleteAccountDialog -> openConfirmDeleteAccountDialog()
            is Action.OpenConfirmLogoutDialog -> openConfirmLogoutDialog()
            is Action.UpdateDarkThemeConfig -> updateDarkThemeConfig(action.darkThemeConfig)
            is Action.UpdateUseCustomTheme -> updateUseCustomTheme(action.customTheme)
            is Action.UpdateUseDynamicColor -> updateUseDynamicColor(action.useDynamicColor)
            is Action.UpdateGeneralSettings -> updateGeneralSettings(action.generalSettings)
            is Action.UpdatePrivacySettings -> updatePrivacySettings(action.privacySettings)
            is Action.UpdateExpandedSubSection -> updateExpandedSection(action.section)
        }
    }

    private fun updateUseCustomTheme(customTheme: ThemeBrand) = launch {
        _settingsConfig.value = _settingsConfig.value?.copy(customTheme = customTheme)
        useCases.updateSetting(themeBrand = customTheme)
    }

    private fun updateUseDynamicColor(useDynamicColor: Boolean) = launch {
        _settingsConfig.value = _settingsConfig.value?.copy(useDynamicColor = useDynamicColor)
        useCases.updateSetting(useDynamicColor = useDynamicColor)
    }

    private fun updateDarkThemeConfig(darkThemeConfig: DarkThemeConfig) = launch {
        _settingsConfig.value = _settingsConfig.value?.copy(darkThemeConfig = darkThemeConfig)
        useCases.updateSetting(darkThemeConfig = darkThemeConfig)
    }

    private fun updateGeneralSettings(generalSettings: GeneralSettings) = launch {
        _settingsConfig.value = _settingsConfig.value?.copy(generalSettings = generalSettings)
        useCases.updateSetting(generalSettings = generalSettings)
    }

    private fun updatePrivacySettings(privacySettings: PrivacySettings) = launch {
        _settingsConfig.value = _settingsConfig.value?.copy(privacySettings = privacySettings)
        useCases.updateSetting(privacySettings = privacySettings)
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
        handleSuccess()
    }

    private fun deleteAccount() = launch {
        useCases.deleteAccount.invoke().first().fold(
            ifLeft = ::handleError,
            ifRight = {
                logout()
            }
        )
    }

    private fun handleSuccess() = launch {
        sendEvent(OneTimeEvent.SuccessNavigation())
    }

    private fun updateExpandedSection(
        section: SettingsSubSection
    ) = launch {
        // TODO: implement
    }

    sealed class Action {
        object FetchData : Action()
        object Logout : Action()
        object DeleteAccount : Action()
        object OpenConfirmDeleteAccountDialog : Action()
        object CloseConfirmDeleteAccountDialog : Action()
        object OpenConfirmLogoutDialog : Action()
        object CloseConfirmLogoutDialog : Action()
        class UpdateUseCustomTheme(val customTheme: ThemeBrand) : Action()
        class UpdateUseDynamicColor(val useDynamicColor: Boolean) : Action()
        class UpdateDarkThemeConfig(val darkThemeConfig: DarkThemeConfig) : Action()
        class UpdateGeneralSettings(val generalSettings: GeneralSettings) : Action()
        class UpdatePrivacySettings(val privacySettings: PrivacySettings) : Action()
        class UpdateExpandedSubSection(val section: SettingsSubSection) : Action()
    }

    data class State(
        val loadingState: LoadingState = LoadingState.Loaded,
        val settingsSection: List<SettingsSection> = emptyList(),
        val userEmail: String? = null,
        val openConfirmDeleteAccountDialog: Boolean = false,
        val openConfirmLogoutDialog: Boolean = false,
        val settingsConfig: SettingsConfig? = null
    )

}