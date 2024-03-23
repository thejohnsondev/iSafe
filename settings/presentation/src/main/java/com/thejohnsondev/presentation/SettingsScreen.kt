package com.thejohnsondev.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import com.thejohnsondev.common.R
import com.thejohnsondev.common.toast
import com.thejohnsondev.designsystem.ISafeTheme
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size8
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.ui.RoundedButton
import com.thejohnsondev.ui.ScaffoldConfig

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
        onLogout = {
        viewModel.perform(SettingsViewModel.Action.Logout)
        goToSignUp()
    }, onDeleteAccount = {
        viewModel.perform(SettingsViewModel.Action.DeleteAccount)
    })
}

@Composable
fun SettingsContent(
    state: SettingsViewModel.State,
    onLogout: () -> Unit = { },
    onDeleteAccount: () -> Unit = { }
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Row {
            Text(
                text = state.userEmail ?: "",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(Size8)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            RoundedButton(
                modifier = Modifier
                    .padding(horizontal = Size8),
                text = stringResource(id = R.string.delete_account),
                onClick = {
                    // TODO: add popup for confirmation
                    onDeleteAccount()
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
                    onLogout()
                }
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsScreenPreviewDark() {
    ISafeTheme {
        SettingsContent(
            onLogout = {},
            onDeleteAccount = {},
            state = SettingsViewModel.State(
                loadingState = LoadingState.Loaded,
                userEmail = "email@email.com"
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun SettingsScreenPreviewLight() {
    ISafeTheme {
        SettingsContent(
            onLogout = {},
            onDeleteAccount = {},
            state = SettingsViewModel.State(
                loadingState = LoadingState.Loaded,
                userEmail = "email@email.com"
            )
        )
    }
}