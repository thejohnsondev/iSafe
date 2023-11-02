package com.thejohnsondev.presentation.create_encryption_key

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.thejohnsondev.common.toast
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size86
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.ui.ISafeFileLoader

@Composable
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun CreateEncryptionKeyScreen(
    viewModel: CreateEncryptionKeyViewModel,
    onGoToHomeScreen: () -> Unit,
    onGoToSignUpScreen: () -> Unit
) {
    val context = LocalContext.current
    val screenState =
        viewModel.viewState.collectAsState(initial = CreateEncryptionKeyViewModel.State())
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(true) {
        viewModel.getEventFlow().collect {
            when (it) {
                is OneTimeEvent.InfoToast -> context.toast(it.message)
                is OneTimeEvent.InfoSnackbar -> snackbarHostState.showSnackbar(
                    it.message,
                    duration = SnackbarDuration.Short
                )

                is OneTimeEvent.SuccessNavigation -> {
                    onGoToHomeScreen()
                }
            }
        }
    }

    StatusBarColor()
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    modifier = Modifier
                        .padding(bottom = Size86, start = Size16, end = Size16),
                ) {
                    Text(text = data.visuals.message)
                }
            }
        },
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = stringResource(com.thejohnsondev.common.R.string.key_generation),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }, navigationIcon = {
                Icon(
                    modifier = Modifier
                        .padding(Size16)
                        .clickable {
                            viewModel.perform(CreateEncryptionKeyViewModel.Action.Logout)
                            onGoToSignUpScreen()
                        },
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(com.thejohnsondev.common.R.string.arrow_back),
                    tint = MaterialTheme.colorScheme.onSurface,
                )

            }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            ISafeFileLoader(
                isLoading = screenState.value.loadingState == LoadingState.Loading
            ) {
                viewModel.perform(CreateEncryptionKeyViewModel.Action.GenerateKey(it))
            }
        }
    }
}

@Composable
fun StatusBarColor() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(MaterialTheme.colorScheme.background)
}