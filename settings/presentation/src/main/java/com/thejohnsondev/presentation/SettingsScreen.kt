package com.thejohnsondev.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.thejohnsondev.common.R
import com.thejohnsondev.common.toast
import com.thejohnsondev.designsystem.ISafeTheme
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size8
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.ui.AccountSettingsItem
import com.thejohnsondev.ui.ConfirmAlertDialog
import com.thejohnsondev.ui.RoundedButton
import com.thejohnsondev.ui.ScaffoldConfig
import com.thejohnsondev.ui.bounceClick

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
        })
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
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .padding(Size16)
        ) {
            AccountSettingsItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .bounceClick(),
                accountName = state.userEmail,
                onItemClick = {
                    // todo open account settings
                })
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            RoundedButton(
                modifier = Modifier
                    .padding(Size8),
                text = stringResource(id = R.string.delete_account),
                onClick = {
                    onDeleteAccountClick()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            )
            RoundedButton(
                modifier = Modifier
                    .padding(start = Size8, end = Size8, bottom = Size16),
                text = stringResource(id = R.string.logout),
                onClick = {
                    onLogoutClick()
                }
            )
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
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
            onLogoutClick = {}
        )
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsScreenPreviewDarkConfirmDeleteAccount() {
    ISafeTheme {
        SettingsContent(
            onLogoutConfirm = {},
            state = SettingsViewModel.State(
                loadingState = LoadingState.Loaded,
                userEmail = "email@email.com",
                openConfirmLogoutDialog = true
            ), onDeleteAccountConfirm = {},
            onDeleteAccountCancel = {},
            onDeleteAccountClick = {},
            onLogoutCancel = {},
            onLogoutClick = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun SettingsScreenPreviewLight() {
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
            onLogoutClick = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
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
            onLogoutClick = {}
        )
    }
}