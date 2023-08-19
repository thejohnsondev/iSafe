package com.thejohnsondev.isafe.presentation.screens.create_encryption_key

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.thejohnsondev.isafe.R
import com.thejohnsondev.isafe.domain.models.LoadingState
import com.thejohnsondev.isafe.domain.models.OneTimeEvent
import com.thejohnsondev.isafe.presentation.components.UploadFileButton
import com.thejohnsondev.isafe.utils.Size150
import com.thejohnsondev.isafe.utils.Size16
import com.thejohnsondev.isafe.utils.Size86
import com.thejohnsondev.isafe.utils.toast

@Composable
fun CreateEncryptionKeyScreen(
    navController: NavHostController,
    viewModel: CreateEncryptionKeyViewModel
) {
    CreateEncryptionKeyContent(navController, viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateEncryptionKeyContent(
    navController: NavHostController,
    viewModel: CreateEncryptionKeyViewModel
) {
    val context = LocalContext.current
    val screenState = viewModel.viewState.collectAsState(initial = CreateEncryptionKeyViewState())
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val singleFilePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) {
        viewModel.perform(CreateEncryptionKeyAction.GenerateKey(it))
    }

    LaunchedEffect(true) {
        viewModel.getEventFlow().collect {
            when (it) {
                is OneTimeEvent.InfoToast -> context.toast(it.message)
                is OneTimeEvent.InfoSnackbar -> snackbarHostState.showSnackbar(
                    it.message,
                    duration = SnackbarDuration.Short
                )

                is OneTimeEvent.SuccessNavigation -> {}
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
                    text = stringResource(R.string.key_generation),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }, navigationIcon = {
                Icon(
                    modifier = Modifier.padding(Size16),
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.arrow_back),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                navController.popBackStack()
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                UploadFileButton(
                    modifier = Modifier.size(Size150),
                    onUploadClick = {
                        singleFilePickerLauncher.launch(
                            PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    isLoading = screenState.value.loadingState == LoadingState.Loading
                )
                Text(
                    modifier = Modifier.padding(Size16),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    text = stringResource(R.string.click_to_upload_your_key_file)
                )
            }
        }
    }
}


@Composable
fun StatusBarColor() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(MaterialTheme.colorScheme.background)
}