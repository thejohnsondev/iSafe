package com.thejohnsondev.presentation.login

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.thejohnsondev.common.EMPTY
import com.thejohnsondev.common.getEmailErrorMessage
import com.thejohnsondev.common.getPasswordErrorMessage
import com.thejohnsondev.common.toast
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size24
import com.thejohnsondev.designsystem.Size4
import com.thejohnsondev.designsystem.Size8
import com.thejohnsondev.designsystem.Size86
import com.thejohnsondev.designsystem.TopRounded
import com.thejohnsondev.model.EmailValidationState
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.model.PasswordValidationState
import com.thejohnsondev.ui.ISafeLogo
import com.thejohnsondev.ui.RoundedButton
import com.thejohnsondev.ui.TextField

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    goToEnterKey: () -> Unit,
    goBack: () -> Unit
) {
    LoginContent(
        viewModel = viewModel,
        onGoToEnterKeyScreen = goToEnterKey,
        onGoBack = goBack
    )
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginContent(
    viewModel: LoginViewModel,
    onGoToEnterKeyScreen: () -> Unit,
    onGoBack: () -> Unit
) {
    val context = LocalContext.current
    val screenState = viewModel.viewState.collectAsState(initial = LoginViewModel.State())
    val emailState = rememberSaveable {
        mutableStateOf(EMPTY)
    }
    val passwordState = rememberSaveable {
        mutableStateOf(EMPTY)
    }
    val passwordFocusRequest = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
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
                    onGoToEnterKeyScreen()
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
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Column(
                modifier = Modifier
                    .padding(top = Size8)
                    .scrollable(rememberScrollState(), Orientation.Vertical),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LogoSection()
                FieldsSection(
                    context = context,
                    viewModel = viewModel,
                    screenState = screenState,
                    emailState = emailState,
                    passwordState = passwordState,
                    passwordFocusRequest = passwordFocusRequest,
                    keyboardController = keyboardController,
                    onGoBack = onGoBack
                )
            }
            LoginButtonSection(
                screenState = screenState,
                viewModel = viewModel,
                emailState = emailState,
                passwordState = passwordState
            )
        }
    }
}


@Composable
fun StatusBarColor() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(MaterialTheme.colorScheme.surfaceVariant)
}

@Composable
fun LogoSection() {
    ISafeLogo(modifier = Modifier)
    Spacer(modifier = Modifier.height(Size24))
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FieldsSection(
    context: Context,
    viewModel: LoginViewModel,
    screenState: State<LoginViewModel.State>,
    emailState: MutableState<String>,
    passwordState: MutableState<String>,
    passwordFocusRequest: FocusRequester,
    keyboardController: SoftwareKeyboardController?,
    onGoBack: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxHeight(),
        color = MaterialTheme.colorScheme.surface,
        shape = TopRounded.medium
    ) {
        Column {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(Size16),
                text = stringResource(com.thejohnsondev.common.R.string.log_in),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(Size8))
            TextField(
                textState = emailState,
                onTextChanged = {
                    emailState.value = it
                    viewModel.perform(LoginViewModel.Action.ValidateEmail(it))
                },
                label = stringResource(com.thejohnsondev.common.R.string.email),
                onKeyboardAction = KeyboardActions {
                    passwordFocusRequest.requestFocus()

                }, imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email,
                isError = screenState.value.emailValidationState !is EmailValidationState.EmailCorrectState,
                errorText = if (screenState.value.emailValidationState is EmailValidationState.EmailIncorrectState) context.getEmailErrorMessage(
                    (screenState.value.emailValidationState as EmailValidationState.EmailIncorrectState).reason
                )
                else null
            )
            Spacer(modifier = Modifier.height(Size8))
            TextField(
                modifier = Modifier
                    .focusRequester(passwordFocusRequest),
                textState = passwordState,
                onTextChanged = {
                    passwordState.value = it
                    viewModel.perform(LoginViewModel.Action.ValidatePassword(it))
                },
                label = stringResource(com.thejohnsondev.common.R.string.password),
                imeAction = ImeAction.Done,
                onKeyboardAction = KeyboardActions {
                    keyboardController?.hide()
                },
                keyboardType = KeyboardType.Password,
                isError = screenState.value.passwordValidationState !is PasswordValidationState.PasswordCorrectState,
                errorText = if (screenState.value.passwordValidationState is PasswordValidationState.PasswordIncorrectState) context.getPasswordErrorMessage(
                    (screenState.value.passwordValidationState as PasswordValidationState.PasswordIncorrectState).reason
                ) else null
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(Size16)
            ) {
                Text(
                    text = stringResource(com.thejohnsondev.common.R.string.don_t_have_an_account),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(com.thejohnsondev.common.R.string.sign_up),
                    modifier = Modifier
                        .padding(start = Size4)
                        .clickable {
                            onGoBack
                        },
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun LoginButtonSection(
    screenState: State<LoginViewModel.State>,
    viewModel: LoginViewModel,
    emailState: MutableState<String>,
    passwordState: MutableState<String>
) {
    Column(verticalArrangement = Arrangement.Bottom) {
        RoundedButton(
            text = stringResource(id = com.thejohnsondev.common.R.string.log_in),
            modifier = Modifier.padding(Size8),
            enabled = screenState.value.loginReady,
            onClick = {
                viewModel.perform(
                    LoginViewModel.Action.LoginWithEmail(
                        emailState.value,
                        passwordState.value
                    )
                )
            },
            loading = screenState.value.loadingState is LoadingState.Loading
        )
    }
}