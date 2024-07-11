package com.thejohnsondev.presentation

import androidx.lifecycle.viewModelScope
import com.thejohnsondev.common.base.BaseViewModel
import com.thejohnsondev.common.combine
import com.thejohnsondev.common.isPasswordValid
import com.thejohnsondev.domain.SettingsUseCases
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.model.PasswordValidationState
import com.thejohnsondev.model.settings.DarkThemeConfig
import com.thejohnsondev.model.settings.GeneralSettings
import com.thejohnsondev.model.settings.PrivacySettings
import com.thejohnsondev.model.settings.SettingsConfig
import com.thejohnsondev.model.settings.ThemeBrand
import com.thejohnsondev.ui.ui_model.SettingsSection
import com.thejohnsondev.ui.ui_model.SettingsSubSection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val _openChangePasswordDialog = MutableStateFlow(false)
    private val _newPasswordValidationState = MutableStateFlow<PasswordValidationState?>(null)
    private val _isConfirmNewPasswordMatches = MutableStateFlow<Boolean?>(null)
    private val _updatePasswordLoadingState = MutableStateFlow<LoadingState>(LoadingState.Loaded)

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
        _updatePasswordLoadingState,
        _openChangePasswordDialog,
        _newPasswordValidationState,
        _isConfirmNewPasswordMatches,
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
            is Action.CloseChangePasswordDialog -> handleCloseChangePasswordDialog()
            is Action.OpenChangePasswordDialog -> handleOpenChangePasswordDialog()
            is Action.ChangePassword -> changePassword(action.oldPassword, action.newPassword)
            is Action.EnterConfirmPassword -> handleConfirmPassword(
                action.confirmPassword,
                action.newPassword
            )

            is Action.ValidateNewPassword -> validateNewPassword(action.newPassword, action.confirmPassword)
        }
    }

    private fun changePassword(
        oldPassword: String,
        newPassword: String,
    ) = viewModelScope.launch(Dispatchers.IO) {
        _updatePasswordLoadingState.value = LoadingState.Loading
        useCases.changePassword.invoke(
            oldPassword, newPassword
        ).first().fold(ifLeft = {
            _updatePasswordLoadingState.value = LoadingState.Loaded
            handleError(it)
        }, ifRight = {
            _updatePasswordLoadingState.value = LoadingState.Loaded
            handlePasswordChangeSuccess()
        })
    }

    private fun handlePasswordChangeSuccess() = launch {
        sendEvent(PasswordChangeSuccess("Password changed successfully")) // todo move to strings and use string resource provider
    }

    private fun validateNewPassword(newPassword: String, confirmPassword: String?) {
        _newPasswordValidationState.value = newPassword.isPasswordValid()
        if (!confirmPassword.isNullOrEmpty()) {
            handleConfirmPassword(confirmPassword, newPassword)
        }
    }

    private fun handleConfirmPassword(confirmPassword: String, newPassword: String) {
        _isConfirmNewPasswordMatches.value = newPassword == confirmPassword
    }

    private fun handleOpenChangePasswordDialog() {
        _openChangePasswordDialog.value = true
    }

    private fun handleCloseChangePasswordDialog() {
        _isConfirmNewPasswordMatches.value = null
        _newPasswordValidationState.value = null
        _openChangePasswordDialog.value = false
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
        object OpenChangePasswordDialog : Action()
        object CloseChangePasswordDialog : Action()
        class UpdateUseCustomTheme(val customTheme: ThemeBrand) : Action()
        class UpdateUseDynamicColor(val useDynamicColor: Boolean) : Action()
        class UpdateDarkThemeConfig(val darkThemeConfig: DarkThemeConfig) : Action()
        class UpdateGeneralSettings(val generalSettings: GeneralSettings) : Action()
        class UpdatePrivacySettings(val privacySettings: PrivacySettings) : Action()
        class UpdateExpandedSubSection(val section: SettingsSubSection) : Action()
        data class ChangePassword(
            val oldPassword: String,
            val newPassword: String
        ) : Action()

        data class ValidateNewPassword(val newPassword: String, val confirmPassword: String?) : Action()
        data class EnterConfirmPassword(
            val confirmPassword: String,
            val newPassword: String
        ) : Action()
    }

    data class State(
        val loadingState: LoadingState = LoadingState.Loaded,
        val settingsSection: List<SettingsSection> = emptyList(),
        val userEmail: String? = null,
        val openConfirmDeleteAccountDialog: Boolean = false,
        val openConfirmLogoutDialog: Boolean = false,
        val settingsConfig: SettingsConfig? = null,
        val updatePasswordLoadingState: LoadingState = LoadingState.Loaded,
        val openChangePasswordDialog: Boolean = false,
        val newPasswordValidationState: PasswordValidationState? = null,
        val isConfirmPasswordMatches: Boolean? = null
    )

    data class PasswordChangeSuccess(
        val message: String = "Password changed successfully"
    ) : OneTimeEvent()

}