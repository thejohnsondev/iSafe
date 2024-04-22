package com.thejohnsondev.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Person
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
import com.thejohnsondev.model.settings.ThemeBrand
import com.thejohnsondev.ui.ConfirmAlertDialog
import com.thejohnsondev.ui.OptionItem
import com.thejohnsondev.ui.RoundedButton
import com.thejohnsondev.ui.ScaffoldConfig
import com.thejohnsondev.ui.SettingsItem

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
        onDeleteAccountConfirm = {
            viewModel.perform(SettingsViewModel.Action.CloseConfirmDeleteAccountDialog)
            viewModel.perform(SettingsViewModel.Action.DeleteAccount)
        }, onDeleteAccountCancel = {
            viewModel.perform(SettingsViewModel.Action.CloseConfirmDeleteAccountDialog)
        }, onDeleteAccountClick = {
            viewModel.perform(SettingsViewModel.Action.OpenConfirmDeleteAccountDialog)
        },
        onLogoutConfirm = {
            viewModel.perform(SettingsViewModel.Action.CloseConfirmLogoutDialog)
            viewModel.perform(SettingsViewModel.Action.Logout)
            goToSignUp()
        },
        onLogoutClick = {
            viewModel.perform(SettingsViewModel.Action.OpenConfirmLogoutDialog)
        },
        onLogoutCancel = {
            viewModel.perform(SettingsViewModel.Action.CloseConfirmLogoutDialog)
        },
        onThemeBrandingSelect = {
            viewModel.perform(SettingsViewModel.Action.UpdateUseCustomTheme(it))
        },
        onSupportDynamicTheme = {
            viewModel.perform(SettingsViewModel.Action.UpdateUseDynamicColor(it))
        },
        onDarkThemeConfigSelect = {
            viewModel.perform(SettingsViewModel.Action.UpdateDarkThemeConfig(it))
        }
    )
}

@Composable
fun SettingsContent(
    state: SettingsViewModel.State,
    onDeleteAccountClick: () -> Unit,
    onDeleteAccountConfirm: () -> Unit,
    onDeleteAccountCancel: () -> Unit,
    onLogoutClick: () -> Unit,
    onLogoutConfirm: () -> Unit,
    onLogoutCancel: () -> Unit,
    onThemeBrandingSelect: (ThemeBrand) -> Unit,
    onSupportDynamicTheme: (Boolean) -> Unit,
    onDarkThemeConfigSelect: (DarkThemeConfig) -> Unit
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
                        onDeleteAccountClick()
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
                        onLogoutClick()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentColor = MaterialTheme.colorScheme.primaryContainer
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
                title = stringResource(id = R.string.setting_title_style),
                description = stringResource(id = R.string.setting_description_style),
                icon = Icons.Default.FormatPaint,
                isFirstItem = true
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
                    OptionItem(
                        modifier = Modifier
                            .padding(top = Size4),
                        optionTitle = stringResource(id = R.string.theme_default),
                        isFirstItem = true,
                        isSelected = state.settingsConfig?.customTheme == ThemeBrand.DEFAULT
                    ) {
                        onThemeBrandingSelect(ThemeBrand.DEFAULT)
                    }
                    OptionItem(
                        modifier = Modifier
                            .padding(top = Size4),
                        optionTitle = stringResource(id = R.string.theme_android),
                        isLastItem = true,
                        isSelected = state.settingsConfig?.customTheme == ThemeBrand.ANDROID
                    ) {
                        onSupportDynamicTheme(false)
                        onThemeBrandingSelect(ThemeBrand.ANDROID)
                    }
                    if(state.settingsConfig?.customTheme == ThemeBrand.DEFAULT && supportsDynamicTheming()) {
                        Text(
                            modifier = Modifier.padding(bottom = Size8, top = Size16),
                            text = stringResource(id = R.string.use_dynamic_color),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                        OptionItem(
                            modifier = Modifier
                                .padding(top = Size4),
                            optionTitle = stringResource(id = R.string.yes),
                            isFirstItem = true,
                            isSelected = state.settingsConfig.useDynamicColor
                        ) {
                            onSupportDynamicTheme(true)
                        }
                        OptionItem(
                            modifier = Modifier
                                .padding(top = Size4),
                            optionTitle = stringResource(id = R.string.no),
                            isLastItem = true,
                            isSelected = !state.settingsConfig.useDynamicColor
                        ) {
                            onSupportDynamicTheme(false)
                        }
                    }
                    Text(
                        modifier = Modifier.padding(bottom = Size8, top = Size16),
                        text = stringResource(id = R.string.dark_mode_preference),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    OptionItem(
                        modifier = Modifier
                            .padding(top = Size4),
                        optionTitle = stringResource(id = R.string.dark_mode_preference_system),
                        isFirstItem = true,
                        isSelected = state.settingsConfig?.darkThemeConfig == DarkThemeConfig.SYSTEM
                    ) {
                        onDarkThemeConfigSelect(DarkThemeConfig.SYSTEM)
                    }
                    OptionItem(
                        modifier = Modifier
                            .padding(top = Size4),
                        optionTitle = stringResource(id = R.string.dark_mode_preference_dark),
                        isSelected = state.settingsConfig?.darkThemeConfig == DarkThemeConfig.DARK
                    ) {
                        onDarkThemeConfigSelect(DarkThemeConfig.DARK)
                    }
                    OptionItem(
                        modifier = Modifier
                            .padding(top = Size4),
                        optionTitle = stringResource(id = R.string.dark_mode_preference_light),
                        isLastItem = true,
                        isSelected = state.settingsConfig?.darkThemeConfig == DarkThemeConfig.LIGHT
                    ) {
                        onDarkThemeConfigSelect(DarkThemeConfig.LIGHT)
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
                        onDeleteAccountConfirm()
                    },
                    onCancel = {
                        onDeleteAccountCancel()
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
                        onLogoutConfirm()
                    },
                    onCancel = {
                        onLogoutCancel()
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
            onLogoutConfirm = {},
            state = SettingsViewModel.State(
                loadingState = LoadingState.Loaded,
                userEmail = "email@email.com"
            ),
            onDeleteAccountConfirm = {},
            onDeleteAccountCancel = {},
            onDeleteAccountClick = {},
            onLogoutCancel = {},
            onLogoutClick = {},
            onThemeBrandingSelect = {},
            onSupportDynamicTheme = {},
            onDarkThemeConfigSelect = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun SettingsScreenPreviewLightConfirmDeleteAccount() {
    ISafeTheme {
        SettingsContent(
            onLogoutConfirm = {},
            state = SettingsViewModel.State(
                loadingState = LoadingState.Loaded,
                userEmail = "email@email.com",
                openConfirmDeleteAccountDialog = true
            ),
            onDeleteAccountConfirm = {},
            onDeleteAccountCancel = {},
            onDeleteAccountClick = {},
            onLogoutCancel = {},
            onLogoutClick = {},
            onThemeBrandingSelect = {},
            onSupportDynamicTheme = {},
            onDarkThemeConfigSelect = {}
        )
    }
}