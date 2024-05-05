package com.thejohnsondev.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.thejohnsondev.common.R
import com.thejohnsondev.common.toast
import com.thejohnsondev.designsystem.ISafeTheme
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size4
import com.thejohnsondev.designsystem.Size8
import com.thejohnsondev.designsystem.supportsDynamicTheming
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.model.settings.DarkThemeConfig
import com.thejohnsondev.model.settings.GeneralSettings
import com.thejohnsondev.model.settings.ThemeBrand
import com.thejohnsondev.ui.ConfirmAlertDialog
import com.thejohnsondev.ui.RoundedButton
import com.thejohnsondev.ui.ScaffoldConfig
import com.thejohnsondev.ui.SelectableOptionItem
import com.thejohnsondev.ui.SettingsItem
import com.thejohnsondev.ui.ToggleOptionItem

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    goToSignUp: () -> Unit,
    setScaffoldConfig: (ScaffoldConfig) -> Unit
) {
    val context = LocalContext.current
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val state = viewModel.viewState.collectAsState(SettingsViewModel.State())
    LaunchedEffect(true) {
        viewModel.perform(SettingsViewModel.Action.FetchData)
        viewModel.getEventFlow().collect {
            when (it) {
                is OneTimeEvent.InfoToast -> context.toast(it.message)
                is OneTimeEvent.InfoSnackbar -> snackBarHostState.showSnackbar(
                    it.message,
                    duration = SnackbarDuration.Short
                )

                is OneTimeEvent.SuccessNavigation -> {
                    goToSignUp()
                }

            }
        }
    }
    setScaffoldConfig(
        ScaffoldConfig(
            isTopAppBarVisible = true,
            isBottomNavBarVisible = true,
            topAppBarTitle = stringResource(R.string.settings),
        )
    )
    SettingsContent(
        state = state.value,
        onAction = { action ->
            viewModel.perform(action)
        },
        goToSignUp = goToSignUp
    )
}

@Composable
fun SettingsContent(
    state: SettingsViewModel.State,
    onAction: (SettingsViewModel.Action) -> Unit,
    goToSignUp: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Text(
                modifier = Modifier
                    .padding(Size16)
                    .align(Alignment.Start),
                text = stringResource(id = R.string.account),
                style = MaterialTheme.typography.titleLarge
            )
            SettingsItem(
                title = stringResource(id = R.string.manage_account),
                description = state.userEmail.orEmpty(),
                icon = Icons.Default.Person,
                isFirstItem = true,
                isLastItem = true
            ) {
                RoundedButton(
                    modifier = Modifier
                        .padding(horizontal = Size16, vertical = Size8),
                    text = stringResource(id = R.string.delete_account),
                    onClick = {
                        onAction(SettingsViewModel.Action.OpenConfirmDeleteAccountDialog)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                )
                RoundedButton(
                    modifier = Modifier
                        .padding(start = Size16, end = Size16, bottom = Size16, top = Size8),
                    text = stringResource(id = R.string.logout),
                    onClick = {
                        onAction(SettingsViewModel.Action.OpenConfirmLogoutDialog)
                    },

                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
            Text(
                modifier = Modifier
                    .padding(Size16)
                    .align(Alignment.Start),
                text = stringResource(id = R.string.settings),
                style = MaterialTheme.typography.titleLarge
            )
            SettingsItem(
                title = stringResource(id = R.string.general_title),
                description = stringResource(id = R.string.general_description),
                icon = Icons.Default.Settings,
                isFirstItem = true,
                isLastItem = false
            ) {
                Column(
                    modifier = Modifier.padding(Size16)
                ) {
                    ToggleOptionItem(
                        optionTitle = stringResource(id = R.string.deep_search_title),
                        isSelected = state.settingsConfig?.generalSettings?.isDeepSearchEnabled
                            ?: false,
                        isFirstItem = true,
                        isLastItem = true
                    ) {
                        onAction(SettingsViewModel.Action.UpdateGeneralSettings(
                            state.settingsConfig?.generalSettings?.copy(
                                isDeepSearchEnabled = it
                            ) ?: GeneralSettings(isDeepSearchEnabled = it)
                        ))
                    }
                }
            }
            SettingsItem(
                title = stringResource(id = R.string.setting_title_style),
                description = stringResource(id = R.string.setting_description_style),
                icon = Icons.Default.FormatPaint
            ) {
                Column(
                    modifier = Modifier.padding(Size16)
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = Size8),
                        text = stringResource(id = R.string.theme),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    SelectableOptionItem(
                        modifier = Modifier
                            .padding(top = Size4),
                        optionTitle = stringResource(id = R.string.theme_default),
                        isFirstItem = true,
                        isSelected = state.settingsConfig?.customTheme == ThemeBrand.DEFAULT
                    ) {
                        onAction(SettingsViewModel.Action.UpdateUseCustomTheme(ThemeBrand.DEFAULT))
                    }
                    SelectableOptionItem(
                        modifier = Modifier
                            .padding(top = Size4),
                        optionTitle = stringResource(id = R.string.theme_android),
                        isLastItem = true,
                        isSelected = state.settingsConfig?.customTheme == ThemeBrand.ANDROID
                    ) {
                        onAction(SettingsViewModel.Action.UpdateUseDynamicColor(false))
                        onAction(SettingsViewModel.Action.UpdateUseCustomTheme(ThemeBrand.ANDROID))
                    }
                    if (state.settingsConfig?.customTheme == ThemeBrand.DEFAULT && supportsDynamicTheming()) {
                        Text(
                            modifier = Modifier.padding(bottom = Size8, top = Size16),
                            text = stringResource(id = R.string.use_dynamic_color),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                        SelectableOptionItem(
                            modifier = Modifier
                                .padding(top = Size4),
                            optionTitle = stringResource(id = R.string.yes),
                            isFirstItem = true,
                            isSelected = state.settingsConfig.useDynamicColor
                        ) {
                            onAction(SettingsViewModel.Action.UpdateUseDynamicColor(true))
                        }
                        SelectableOptionItem(
                            modifier = Modifier
                                .padding(top = Size4),
                            optionTitle = stringResource(id = R.string.no),
                            isLastItem = true,
                            isSelected = !state.settingsConfig.useDynamicColor
                        ) {
                            onAction(SettingsViewModel.Action.UpdateUseDynamicColor(false))
                        }
                    }
                    Text(
                        modifier = Modifier.padding(bottom = Size8, top = Size16),
                        text = stringResource(id = R.string.dark_mode_preference),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    SelectableOptionItem(
                        modifier = Modifier
                            .padding(top = Size4),
                        optionTitle = stringResource(id = R.string.dark_mode_preference_system),
                        isFirstItem = true,
                        isSelected = state.settingsConfig?.darkThemeConfig == DarkThemeConfig.SYSTEM
                    ) {
                        onAction(
                            SettingsViewModel.Action.UpdateDarkThemeConfig(
                                DarkThemeConfig.SYSTEM
                            )
                        )
                    }
                    SelectableOptionItem(
                        modifier = Modifier
                            .padding(top = Size4),
                        optionTitle = stringResource(id = R.string.dark_mode_preference_dark),
                        isSelected = state.settingsConfig?.darkThemeConfig == DarkThemeConfig.DARK
                    ) {
                        onAction(
                            SettingsViewModel.Action.UpdateDarkThemeConfig(
                                DarkThemeConfig.DARK
                            )
                        )
                    }
                    SelectableOptionItem(
                        modifier = Modifier
                            .padding(top = Size4),
                        optionTitle = stringResource(id = R.string.dark_mode_preference_light),
                        isLastItem = true,
                        isSelected = state.settingsConfig?.darkThemeConfig == DarkThemeConfig.LIGHT
                    ) {
                        onAction(
                            SettingsViewModel.Action.UpdateDarkThemeConfig(
                                DarkThemeConfig.LIGHT
                            )
                        )
                    }
                }
            }
            if (state.openConfirmDeleteAccountDialog) {
                ConfirmAlertDialog(
                    title = stringResource(id = R.string.delete_account),
                    message = stringResource(id = R.string.delete_account_confirm_message),
                    confirmButtonText = stringResource(id = R.string.delete_account),
                    cancelButtonText = stringResource(id = R.string.cancel),
                    onConfirm = {
                        onAction(SettingsViewModel.Action.CloseConfirmDeleteAccountDialog)
                        onAction(SettingsViewModel.Action.DeleteAccount)
                    },
                    onCancel = {
                        onAction(SettingsViewModel.Action.CloseConfirmDeleteAccountDialog)
                    }
                )
            }
            if (state.openConfirmLogoutDialog) {
                ConfirmAlertDialog(
                    title = stringResource(id = R.string.logout),
                    message = stringResource(id = R.string.logout_confirm_message),
                    confirmButtonText = stringResource(id = R.string.logout),
                    cancelButtonText = stringResource(id = R.string.cancel),
                    onConfirm = {
                        onAction(SettingsViewModel.Action.CloseConfirmLogoutDialog)
                        onAction(SettingsViewModel.Action.Logout)
                        goToSignUp()
                    },
                    onCancel = {
                        onAction(SettingsViewModel.Action.CloseConfirmLogoutDialog)
                    }
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun SettingsScreenPreviewDark() {
    ISafeTheme {
        SettingsContent(
            state = SettingsViewModel.State(
                loadingState = LoadingState.Loaded,
                userEmail = "email@email.com"
            ),
            onAction = {},
            goToSignUp = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun SettingsScreenPreviewLightConfirmDeleteAccount() {
    ISafeTheme {
        SettingsContent(
            state = SettingsViewModel.State(
                loadingState = LoadingState.Loaded,
                userEmail = "email@email.com",
                openConfirmDeleteAccountDialog = true
            ),
            onAction = {},
            goToSignUp = {}
        )
    }
}