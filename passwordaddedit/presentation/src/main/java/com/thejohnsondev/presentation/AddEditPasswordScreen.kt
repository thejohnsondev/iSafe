package com.thejohnsondev.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.thejohnsondev.common.EMPTY
import com.thejohnsondev.common.toast
import com.thejohnsondev.designsystem.EqualRounded
import com.thejohnsondev.designsystem.Size12
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size24
import com.thejohnsondev.designsystem.Size36
import com.thejohnsondev.designsystem.Size4
import com.thejohnsondev.designsystem.Size8
import com.thejohnsondev.designsystem.Text20
import com.thejohnsondev.designsystem.Text22
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.ui.AddEditTopAppBar
import com.thejohnsondev.ui.AdditionalField
import com.thejohnsondev.ui.FullScreenLoading
import com.thejohnsondev.ui.HintTextField
import com.thejohnsondev.ui.LoadedImage
import com.thejohnsondev.ui.bounceClick

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddEditPasswordScreen(
    viewModel: AddEditPasswordViewModel,
    passwordModel: PasswordModel? = null,
    onGoBackClick: () -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState(AddEditPasswordViewModel.State())
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val organizationFocusRequester = remember {
        FocusRequester()
    }
    val titleFocusRequester = remember {
        FocusRequester()
    }
    val passwordFocusRequester = remember {
        FocusRequester()
    }
    if (passwordModel != null) {
        viewModel.perform(AddEditPasswordViewModel.Action.SetPasswordModelForEdit(passwordModel))
    }
    StatusBarColor()
    LaunchedEffect(true) {
        organizationFocusRequester.requestFocus()
        viewModel.getEventFlow().collect {
            when (it) {
                is OneTimeEvent.InfoToast -> context.toast(it.message)
                is OneTimeEvent.InfoSnackbar -> snackbarHostState.showSnackbar(
                    it.message, duration = SnackbarDuration.Short
                )

                is OneTimeEvent.SuccessNavigation -> {
                    keyboardController?.hide()
                    onGoBackClick()
                }

            }
        }
    }
    Scaffold(topBar = {
        AddEditTopAppBar(onSaveClick = {
            viewModel.perform(AddEditPasswordViewModel.Action.SavePassword)
        }, onNavigateBackClick = {
            onGoBackClick()
        })
    }, snackbarHost = {
        SnackbarHost(snackbarHostState) { data ->
            Snackbar(
                modifier = Modifier.padding(WindowInsets.ime.asPaddingValues())
            ) {
                Text(text = data.visuals.message)
            }
        }
    }) { paddings ->
        when (state.value.loadingState) {
            LoadingState.Loading -> FullScreenLoading()
            LoadingState.Loaded -> {
                AddEditPasswordContent(
                    state = state.value,
                    viewModel = viewModel,
                    paddings = paddings,
                    organizationFocusRequester = organizationFocusRequester,
                    titleFocusRequester = titleFocusRequester,
                    passwordFocusRequester = passwordFocusRequester
                )
            }
        }
    }
}

@Composable
fun AddEditPasswordContent(
    state: AddEditPasswordViewModel.State,
    viewModel: AddEditPasswordViewModel,
    paddings: PaddingValues,
    organizationFocusRequester: FocusRequester,
    titleFocusRequester: FocusRequester,
    passwordFocusRequester: FocusRequester,
) {
    var isPasswordHidden by remember {
        mutableStateOf(false)
    }
    val eyeImage = if (isPasswordHidden) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
    Surface(
        modifier = Modifier
            .padding(paddings)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
        ) {
            Row(
                modifier = Modifier.padding(Size16),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(Size36),
                    color = Color.White,
                    shape = EqualRounded.small
                ) {
                    LoadedImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Size4),
                        imageUrl = EMPTY,
                        errorImageVector = com.thejohnsondev.designsystem.R.drawable.ic_passwords,
                        placeholderResId = com.thejohnsondev.designsystem.R.drawable.ic_passwords,
                        backgroundColor = Color.White
                    )
                }
                HintTextField(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(start = Size16),
                    onValueChanged = {
                        viewModel.perform(AddEditPasswordViewModel.Action.EnterOrganization(it))
                    },
                    value = state.organization,
                    hint = stringResource(id = com.thejohnsondev.common.R.string.organization),
                    focusRequester = organizationFocusRequester,
                    textColor = MaterialTheme.colorScheme.onSurface,
                    fontSize = Text22,
                    maxLines = 2,
                    onKeyboardAction = {
                        titleFocusRequester.requestFocus()
                    },
                    imeAction = ImeAction.Next
                )
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = Size16, end = Size16, top = Size24),
                shape = EqualRounded.small,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                HintTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(Size12),
                    onValueChanged = {
                        viewModel.perform(AddEditPasswordViewModel.Action.EnterTitle(it))
                    },
                    value = state.title,
                    hint = stringResource(id = com.thejohnsondev.common.R.string.title),
                    focusRequester = titleFocusRequester,
                    textColor = MaterialTheme.colorScheme.onSurface,
                    fontSize = Text20,
                    maxLines = 2,
                    onKeyboardAction = {
                        passwordFocusRequester.requestFocus()
                    },
                    imeAction = ImeAction.Next
                )

            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = Size16, end = Size16, top = Size8),
                shape = EqualRounded.small,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HintTextField(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth(0.9f)
                            .padding(Size12),
                        onValueChanged = {
                            viewModel.perform(AddEditPasswordViewModel.Action.EnterPassword(it))
                        },
                        value = state.password,
                        hint = stringResource(id = com.thejohnsondev.common.R.string.password),
                        focusRequester = passwordFocusRequester,
                        textColor = MaterialTheme.colorScheme.onSurface,
                        fontSize = Text20,
                        maxLines = 1,
                        onKeyboardAction = {
                            titleFocusRequester.requestFocus()
                        },
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Password,
                        passwordVisible = !isPasswordHidden
                    )
                    IconButton(onClick = {
                        isPasswordHidden = !isPasswordHidden
                    }) {
                        Icon(
                            modifier = Modifier.padding(end = Size8),
                            imageVector = eyeImage,
                            contentDescription = stringResource(com.thejohnsondev.common.R.string.visibility),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            state.additionalFields.forEachIndexed { index, additionalField ->
                AdditionalField(
                    modifier = Modifier
                        .padding(start = Size16, end = Size16, top = Size8),
                    title = additionalField.title,
                    value = additionalField.value,
                    onTitleChanged = {
                        viewModel.perform(
                            AddEditPasswordViewModel.Action.EnterAdditionalFieldTitle(
                                additionalField.id, it
                            )
                        )
                    },
                    onValueChanged = {
                        viewModel.perform(
                            AddEditPasswordViewModel.Action.EnterAdditionalFieldValue(
                                additionalField.id, it
                            )
                        )
                    }, onDeleteClick = {
                        viewModel.perform(
                            AddEditPasswordViewModel.Action.RemoveAdditionalField(
                                additionalField.id
                            )
                        )
                    })
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Size16)
                    .bounceClick(),
                onClick = {
                    viewModel.perform(
                        AddEditPasswordViewModel.Action.AddAdditionalField(
                            System.currentTimeMillis().toString()
                        )
                    )
                },
                shape = EqualRounded.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
            ) {
                Text(
                    text = stringResource(com.thejohnsondev.common.R.string.add_field),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun StatusBarColor() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(MaterialTheme.colorScheme.surface)
}

@Preview
@Composable
fun AddEditPasswordContentPreview() {
    AddEditPasswordContent(state = AddEditPasswordViewModel.State(),
        viewModel = hiltViewModel(),
        paddings = PaddingValues(0.dp),
        organizationFocusRequester = remember {
            FocusRequester()
        },
        titleFocusRequester = remember {
            FocusRequester()
        },
        passwordFocusRequester = remember {
            FocusRequester()
        })
}