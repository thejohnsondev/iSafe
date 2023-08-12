package com.thejohnsondev.isafe.presentation.screens.signup

import android.annotation.SuppressLint
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.thejohnsondev.isafe.R
import com.thejohnsondev.isafe.domain.models.EmailValidationState
import com.thejohnsondev.isafe.domain.models.LoadingState
import com.thejohnsondev.isafe.domain.models.OneTimeEvent
import com.thejohnsondev.isafe.domain.models.PasswordValidationState
import com.thejohnsondev.isafe.presentation.components.ISafeLogo
import com.thejohnsondev.isafe.presentation.components.RoundedButton
import com.thejohnsondev.isafe.presentation.components.TextField
import com.thejohnsondev.isafe.presentation.navigation.Screens
import com.thejohnsondev.isafe.presentation.ui.theme.TopRounded
import com.thejohnsondev.isafe.utils.EMPTY
import com.thejohnsondev.isafe.utils.Size16
import com.thejohnsondev.isafe.utils.Size24
import com.thejohnsondev.isafe.utils.Size4
import com.thejohnsondev.isafe.utils.Size8
import com.thejohnsondev.isafe.utils.Size86
import com.thejohnsondev.isafe.utils.toast

@Composable
fun SignUpScreen(navController: NavHostController, viewModel: SignUpViewModel) {
    SignUpContent(navController, viewModel)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignUpContent(navController: NavHostController, viewModel: SignUpViewModel) {
    val context = LocalContext.current
    val screenState = viewModel.viewState.collectAsState(initial = SignUpViewState())
    val nameState = rememberSaveable {
        mutableStateOf(EMPTY)
    }
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
    if (screenState.value.isSignUpSuccess == true) {
        navController.navigate(Screens.HomeScreen.name)
    }

    LaunchedEffect(true) {
        viewModel.getEventFlow().collect {
            when (it) {
                is OneTimeEvent.InfoToast -> context.toast(it.message)
                is OneTimeEvent.InfoSnackbar -> snackbarHostState.showSnackbar(
                    it.message,
                    duration = SnackbarDuration.Short
                )

                is OneTimeEvent.SuccessNavigation -> navController.navigate(Screens.HomeScreen.name)
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
                    viewModel = viewModel,
                    screenState = screenState,
                    nameState = nameState,
                    emailState = emailState,
                    passwordState = passwordState,
                    emailFocusRequest = emailFocusRequest,
                    passwordFocusRequest = passwordFocusRequest,
                    keyboardController = keyboardController,
                    navController = navController
                )
            }
            SignUpButtonSection(
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
    viewModel: SignUpViewModel,
    screenState: State<SignUpViewState>,
    nameState: MutableState<String>,
    emailState: MutableState<String>,
    passwordState: MutableState<String>,
    emailFocusRequest: FocusRequester,
    passwordFocusRequest: FocusRequester,
    keyboardController: SoftwareKeyboardController?,
    navController: NavController
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
                text = stringResource(R.string.sign_up),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextField(
                textState = nameState,
                onTextChanged = {
                    nameState.value = it
                    viewModel.perform(SignUpAction.EnterName(it))
                },
                label = stringResource(R.string.name),
                onKeyboardAction = KeyboardActions {
                    emailFocusRequest.requestFocus()
                },
                imeAction = ImeAction.Next
            )
            Spacer(modifier = Modifier.height(Size8))
            TextField(
                modifier = Modifier
                    .focusRequester(emailFocusRequest),
                textState = emailState,
                onTextChanged = {
                    emailState.value = it
                    viewModel.perform(SignUpAction.ValidateEmail(it))
                },
                label = stringResource(R.string.email),
                onKeyboardAction = KeyboardActions {
                    passwordFocusRequest.requestFocus()

                }, imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email,
                isError = screenState.value.emailValidationState !is EmailValidationState.EmailCorrectState,
                errorText = if (screenState.value.emailValidationState is EmailValidationState.EmailIncorrectState) stringResource(
                    id = (screenState.value.emailValidationState as EmailValidationState.EmailIncorrectState).reasonResId
                ) else null
            )
            Spacer(modifier = Modifier.height(Size8))
            TextField(
                modifier = Modifier
                    .focusRequester(passwordFocusRequest),
                textState = passwordState,
                onTextChanged = {
                    passwordState.value = it
                    viewModel.perform(SignUpAction.ValidatePassword(it))
                },
                label = stringResource(R.string.password),
                imeAction = ImeAction.Done,
                onKeyboardAction = KeyboardActions {
                    keyboardController?.hide()
                },
                keyboardType = KeyboardType.Password,
                isError = screenState.value.passwordValidationState !is PasswordValidationState.PasswordCorrectState,
                errorText = if (screenState.value.passwordValidationState is PasswordValidationState.PasswordIncorrectState) stringResource(
                    id = (screenState.value.passwordValidationState as PasswordValidationState.PasswordIncorrectState).reasonResId
                ) else null
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(Size16)
            ) {
                Text(
                    text = stringResource(R.string.already_have_an_account),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(R.string.log_in),
                    modifier = Modifier
                        .padding(start = Size4)
                        .clickable {
                            navController.navigate(Screens.LoginScreen.name)
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
    screenState: State<SignUpViewState>,
    viewModel: SignUpViewModel,
    emailState: MutableState<String>,
    passwordState: MutableState<String>
) {
    Column(verticalArrangement = Arrangement.Bottom) {
        RoundedButton(
            text = stringResource(id = R.string.sign_up),
            modifier = Modifier.padding(bottom = Size16),
            enabled = screenState.value.signUpReady,
            onClick = {
                viewModel.perform(
                    SignUpAction.SignUpWithEmail(
                        emailState.value,
                        passwordState.value
                    )
                )
            },
            loading = screenState.value.loadingState is LoadingState.Loading
        )
    }
}