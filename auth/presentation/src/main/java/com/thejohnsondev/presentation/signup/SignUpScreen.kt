package com.thejohnsondev.presentation.signup

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.thejohnsondev.common.EMPTY
import com.thejohnsondev.common.getEmailErrorMessage
import com.thejohnsondev.common.getPasswordErrorMessage
import com.thejohnsondev.common.toast
import com.thejohnsondev.designsystem.EqualRounded
import com.thejohnsondev.designsystem.ISafeTheme
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size24
import com.thejohnsondev.designsystem.Size4
import com.thejohnsondev.designsystem.Size8
import com.thejohnsondev.designsystem.Size86
import com.thejohnsondev.designsystem.isLight
import com.thejohnsondev.model.EmailValidationState
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.model.PasswordValidationState
import com.thejohnsondev.ui.GlowPulsingBackground
import com.thejohnsondev.ui.ISafeLogo
import com.thejohnsondev.ui.RoundedButton
import com.thejohnsondev.ui.TextField

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    goToHome: () -> Unit,
    goToLogin: () -> Unit
) {
    val screenState = viewModel.viewState.collectAsState(initial = SignUpViewModel.State())
    val context = LocalContext.current
    val emailState = rememberSaveable {
        mutableStateOf(EMPTY)
    }
    val passwordState = rememberSaveable {
        mutableStateOf(EMPTY)
    }
    val emailFocusRequest = remember { FocusRequester() }
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
                    it.message, duration = SnackbarDuration.Short
                )

                is OneTimeEvent.SuccessNavigation -> {
                    goToHome()
                }

            }
        }
    }
    SignUpContent(
        screenState.value,
        emailState,
        passwordState,
        emailFocusRequest,
        passwordFocusRequest,
        snackbarHostState,
        goToLogin,
        validateEmail = {
            viewModel.perform(SignUpViewModel.Action.ValidateEmail(it))
        },
        validatePassword = {
            viewModel.perform(SignUpViewModel.Action.ValidatePassword(it))
        },
        hideKeyboard = {
            keyboardController?.hide()
        },
        sigUpWithEmail = { email, password ->
            viewModel.perform(SignUpViewModel.Action.SignUpWithEmail(email, password))
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignUpContent(
    state: SignUpViewModel.State,
    emailState: MutableState<String>,
    passwordState: MutableState<String>,
    emailFocusRequest: FocusRequester,
    passwordFocusRequest: FocusRequester,
    snackbarHostState: SnackbarHostState,
    onGoToLogin: () -> Unit,
    validateEmail: (String) -> Unit,
    validatePassword: (String) -> Unit,
    hideKeyboard: () -> Unit,
    sigUpWithEmail: (String, String) -> Unit
) {

    Scaffold(snackbarHost = {
        SnackbarHost(snackbarHostState) { data ->
            Snackbar(
                modifier = Modifier.padding(bottom = Size86, start = Size16, end = Size16),
            ) {
                Text(text = data.visuals.message)
            }
        }
    }) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = if (MaterialTheme.colorScheme.isLight()) {
                Color.White
            } else {
                Color.Black
            }
        ) {
            Box {
                Box {
                    GlowPulsingBackground()
                }
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .scrollable(rememberScrollState(), Orientation.Vertical),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LogoSection()
                    FieldsSection(
                        screenState = state,
                        emailState = emailState,
                        passwordState = passwordState,
                        emailFocusRequest = emailFocusRequest,
                        passwordFocusRequest = passwordFocusRequest,
                        onGoToLogin = onGoToLogin,
                        validateEmail = validateEmail,
                        validatePassword = validatePassword,
                        hideKeyboard = hideKeyboard
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = paddingValues.calculateBottomPadding())
                ) {
                    SignUpButtonSection(
                        screenState = state,
                        emailState = emailState,
                        passwordState = passwordState,
                        sigUpWithEmail = sigUpWithEmail
                    )
                }
            }
        }
    }
}

@Composable
fun LogoSection() {
    ISafeLogo(modifier = Modifier)
    Spacer(modifier = Modifier.height(Size24))
}

@Composable
fun FieldsSection(
    screenState: SignUpViewModel.State,
    emailState: MutableState<String>,
    passwordState: MutableState<String>,
    emailFocusRequest: FocusRequester,
    passwordFocusRequest: FocusRequester,
    onGoToLogin: () -> Unit,
    validateEmail: (String) -> Unit,
    validatePassword: (String) -> Unit,
    hideKeyboard: () -> Unit
) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = Size8),
        color = MaterialTheme.colorScheme.surface,
        shape = EqualRounded.medium
    ) {
        Column {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(Size16),
                text = stringResource(com.thejohnsondev.common.R.string.sign_up),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(Size8))
            TextField(
                modifier = Modifier.focusRequester(emailFocusRequest),
                textState = emailState,
                onTextChanged = {
                    emailState.value = it
                    validateEmail(it)

                },
                label = stringResource(com.thejohnsondev.common.R.string.email),
                onKeyboardAction = KeyboardActions {
                    passwordFocusRequest.requestFocus()

                },
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email,
                isError = screenState.emailValidationState !is EmailValidationState.EmailCorrectState,
                errorText = if (screenState.emailValidationState is EmailValidationState.EmailIncorrectState) context.getEmailErrorMessage(
                    screenState.emailValidationState.reason
                )
                else null
            )
            Spacer(modifier = Modifier.height(Size8))
            TextField(
                modifier = Modifier.focusRequester(passwordFocusRequest),
                textState = passwordState,
                onTextChanged = {
                    passwordState.value = it
                    validatePassword(it)
                },
                label = stringResource(com.thejohnsondev.common.R.string.password),
                imeAction = ImeAction.Done,
                onKeyboardAction = KeyboardActions {
                    hideKeyboard()
                },
                keyboardType = KeyboardType.Password,
                isError = screenState.passwordValidationState !is PasswordValidationState.PasswordCorrectState,
                errorText = if (screenState.passwordValidationState is PasswordValidationState.PasswordIncorrectState) context.getPasswordErrorMessage(
                    screenState.passwordValidationState.reason
                )
                else null
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(Size16)
            ) {
                Text(
                    text = stringResource(com.thejohnsondev.common.R.string.already_have_an_account),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(com.thejohnsondev.common.R.string.log_in),
                    modifier = Modifier
                        .padding(start = Size4)
                        .clickable {
                            onGoToLogin()
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
fun SignUpButtonSection(
    screenState: SignUpViewModel.State,
    emailState: MutableState<String>,
    passwordState: MutableState<String>,
    sigUpWithEmail: (String, String) -> Unit
) {
    Column(verticalArrangement = Arrangement.Bottom) {
        RoundedButton(
            text = stringResource(id = com.thejohnsondev.common.R.string.sign_up),
            modifier = Modifier.padding(Size8),
            enabled = screenState.signUpReady,
            onClick = {
                sigUpWithEmail(
                    emailState.value, passwordState.value
                )
            },
            loading = screenState.loadingState is LoadingState.Loading
        )
    }
}

@PreviewLightDark
@Composable
private fun SignUpScreenPreviewEmpty() {
    ISafeTheme {
        SignUpContent(
            state = SignUpViewModel.State(),
            emailState = rememberSaveable { mutableStateOf(EMPTY) },
            passwordState = rememberSaveable { mutableStateOf(EMPTY) },
            emailFocusRequest = remember { FocusRequester() },
            passwordFocusRequest = remember { FocusRequester() },
            snackbarHostState = remember { SnackbarHostState() },
            onGoToLogin = {},
            validateEmail = {},
            validatePassword = {},
            hideKeyboard = {},
            sigUpWithEmail = { _, _ -> }
        )
    }
}