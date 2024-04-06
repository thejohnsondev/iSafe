package com.thejohnsondev.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
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
import com.thejohnsondev.common.EMPTY
import com.thejohnsondev.common.R
import com.thejohnsondev.common.toast
import com.thejohnsondev.designsystem.EqualRounded
import com.thejohnsondev.designsystem.ISafeTheme
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
import com.thejohnsondev.ui.AdditionalField
import com.thejohnsondev.ui.ISafeLoading
import com.thejohnsondev.ui.HintTextField
import com.thejohnsondev.ui.LoadedImage
import com.thejohnsondev.ui.ScaffoldConfig
import com.thejohnsondev.ui.bounceClick

@Composable
fun AddEditPasswordScreen(
    viewModel: AddEditPasswordViewModel,
    passwordModel: PasswordModel? = null,
    onGoBackClick: () -> Unit,
    setScaffoldConfig: (ScaffoldConfig) -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState(AddEditPasswordViewModel.State())
    val snackBarHostState = remember {
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

    LaunchedEffect(true) {
        organizationFocusRequester.requestFocus()
        if (passwordModel != null) {
            viewModel.perform(AddEditPasswordViewModel.Action.SetPasswordModelForEdit(passwordModel))
        }
        viewModel.getEventFlow().collect {
            when (it) {
                is OneTimeEvent.InfoToast -> context.toast(it.message)
                is OneTimeEvent.InfoSnackbar -> snackBarHostState.showSnackbar(
                    it.message, duration = SnackbarDuration.Short
                )

                is OneTimeEvent.SuccessNavigation -> {
                    if (it.message != null) {
                        context.toast(it.message!!)
                    }
                    keyboardController?.hide()
                    onGoBackClick()
                }

            }
        }
    }
    setScaffoldConfig(
        ScaffoldConfig(
            isBottomNavBarVisible = false,
            isTopAppBarVisible = true,
            topAppBarIcon = Icons.Default.ArrowBack,
            isTopAppBarSaveButtonVisible = true,
            onTopAppBarSaveClick = {
                viewModel.perform(AddEditPasswordViewModel.Action.SavePassword)
            },
            onTopAppBarIconClick = {
                onGoBackClick()
            },
            snackBarHostState = snackBarHostState,
            snackBarPaddingHorizontal = Size8,
            snackBarPaddingVertical = Size8,
        )
    )
    when (state.value.loadingState) {
        LoadingState.Loading -> ISafeLoading()
        LoadingState.Loaded -> {
            AddEditPasswordContent(
                state = state.value,
                organizationFocusRequester = organizationFocusRequester,
                titleFocusRequester = titleFocusRequester,
                passwordFocusRequester = passwordFocusRequester,
                onEnterOrganization = {
                    viewModel.perform(AddEditPasswordViewModel.Action.EnterOrganization(it))
                },
                onEnterTitle = {
                    viewModel.perform(AddEditPasswordViewModel.Action.EnterTitle(it))
                },
                onEnterPassword = {
                    viewModel.perform(AddEditPasswordViewModel.Action.EnterPassword(it))
                },
                onEnterAdditionalFieldTitle = { id, title ->
                    viewModel.perform(
                        AddEditPasswordViewModel.Action.EnterAdditionalFieldTitle(
                            id,
                            title
                        )
                    )
                },
                onEnterAdditionalFieldValue = { id, value ->
                    viewModel.perform(
                        AddEditPasswordViewModel.Action.EnterAdditionalFieldValue(
                            id,
                            value
                        )
                    )
                },
                onDeleteAdditionalField = {
                    viewModel.perform(AddEditPasswordViewModel.Action.RemoveAdditionalField(it))
                },
                onAddAdditionalField = {
                    viewModel.perform(AddEditPasswordViewModel.Action.AddAdditionalField(it))
                },
                onTitleRequestFocus = {
                    titleFocusRequester.requestFocus()
                },
                onPasswordRequestFocus = {
                    passwordFocusRequester.requestFocus()
                },
            )
        }
    }
}

@Composable
fun AddEditPasswordContent(
    state: AddEditPasswordViewModel.State,
    organizationFocusRequester: FocusRequester,
    titleFocusRequester: FocusRequester,
    passwordFocusRequester: FocusRequester,
    onTitleRequestFocus: () -> Unit,
    onPasswordRequestFocus: () -> Unit,
    onEnterOrganization: (String) -> Unit,
    onEnterTitle: (String) -> Unit,
    onEnterPassword: (String) -> Unit,
    onEnterAdditionalFieldTitle: (String, String) -> Unit,
    onEnterAdditionalFieldValue: (String, String) -> Unit,
    onDeleteAdditionalField: (String) -> Unit,
    onAddAdditionalField: (String) -> Unit,
) {
    var isPasswordHidden by remember {
        mutableStateOf(false)
    }
    val eyeImage = if (isPasswordHidden) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
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
                    onValueChanged = { organization ->
                        onEnterOrganization(organization)
                    },
                    value = state.organization,
                    focusRequester = organizationFocusRequester,
                    hint = stringResource(id = R.string.organization),
                    textColor = MaterialTheme.colorScheme.onSurface,
                    fontSize = Text22,
                    maxLines = 2,
                    onKeyboardAction = {
                        onTitleRequestFocus()
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
                    onValueChanged = { title ->
                        onEnterTitle(title)
                    },
                    value = state.title,
                    hint = stringResource(id = R.string.title),
                    focusRequester = titleFocusRequester,
                    textColor = MaterialTheme.colorScheme.onSurface,
                    fontSize = Text20,
                    maxLines = 2,
                    onKeyboardAction = {
                        onPasswordRequestFocus()
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
                        onValueChanged = { password ->
                            onEnterPassword(password)
                        },
                        value = state.password,
                        hint = stringResource(id = R.string.password),
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
                            contentDescription = stringResource(R.string.visibility),
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
                    onTitleChanged = { title ->
                        onEnterAdditionalFieldTitle(additionalField.id.orEmpty(), title)
                    },
                    onValueChanged = { value ->
                        onEnterAdditionalFieldValue(additionalField.id.orEmpty(), value)
                    }, onDeleteClick = {
                        onDeleteAdditionalField(additionalField.id.orEmpty())
                    })
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Size16)
                    .bounceClick(),
                onClick = {
                    onAddAdditionalField(
                        System.currentTimeMillis().toString()
                    )
                },
                shape = EqualRounded.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
            ) {
                Text(
                    text = stringResource(R.string.add_field),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun AddEditPasswordPreviewEmptyLight() {
    ISafeTheme {
        AddEditPasswordContent(
            state = AddEditPasswordViewModel.State(),
            titleFocusRequester = FocusRequester(),
            passwordFocusRequester = FocusRequester(),
            organizationFocusRequester = FocusRequester(),
            onTitleRequestFocus = {},
            onPasswordRequestFocus = {},
            onEnterOrganization = {},
            onEnterTitle = {},
            onEnterPassword = {},
            onEnterAdditionalFieldTitle = { _, _ -> },
            onEnterAdditionalFieldValue = { _, _ -> },
            onDeleteAdditionalField = {},
            onAddAdditionalField = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AddEditPasswordPreviewEmptyDark() {
    ISafeTheme {
        AddEditPasswordContent(
            state = AddEditPasswordViewModel.State(),
            titleFocusRequester = FocusRequester(),
            passwordFocusRequester = FocusRequester(),
            organizationFocusRequester = FocusRequester(),
            onTitleRequestFocus = {},
            onPasswordRequestFocus = {},
            onEnterOrganization = {},
            onEnterTitle = {},
            onEnterPassword = {},
            onEnterAdditionalFieldTitle = { _, _ -> },
            onEnterAdditionalFieldValue = { _, _ -> },
            onDeleteAdditionalField = {},
            onAddAdditionalField = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun AddEditPasswordPreviewWithDataLight() {
    ISafeTheme {
        AddEditPasswordContent(
            state = AddEditPasswordViewModel.State(
                organization = "Google",
                title = "Test",
                password = "Pass123$"
            ),
            titleFocusRequester = FocusRequester(),
            passwordFocusRequester = FocusRequester(),
            organizationFocusRequester = FocusRequester(),
            onTitleRequestFocus = {},
            onPasswordRequestFocus = {},
            onEnterOrganization = {},
            onEnterTitle = {},
            onEnterPassword = {},
            onEnterAdditionalFieldTitle = { _, _ -> },
            onEnterAdditionalFieldValue = { _, _ -> },
            onDeleteAdditionalField = {},
            onAddAdditionalField = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AddEditPasswordPreviewWithDataDark() {
    ISafeTheme {
        AddEditPasswordContent(
            state = AddEditPasswordViewModel.State(
                organization = "Google",
                title = "Test",
                password = "Pass123$"
            ),
            titleFocusRequester = FocusRequester(),
            passwordFocusRequester = FocusRequester(),
            organizationFocusRequester = FocusRequester(),
            onTitleRequestFocus = {},
            onPasswordRequestFocus = {},
            onEnterOrganization = {},
            onEnterTitle = {},
            onEnterPassword = {},
            onEnterAdditionalFieldTitle = { _, _ -> },
            onEnterAdditionalFieldValue = { _, _ -> },
            onDeleteAdditionalField = {},
            onAddAdditionalField = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun AddEditPasswordPreviewWithAdditionalFieldsLight() {
    ISafeTheme {
        AddEditPasswordContent(
            state = AddEditPasswordViewModel.State(
                organization = "Google",
                title = "Test",
                password = "Pass123$",
                additionalFields = listOf(
                    com.thejohnsondev.model.AdditionalField(
                        id = "1",
                        title = "Test",
                        value = "Test"
                    ),
                )
            ),
            titleFocusRequester = FocusRequester(),
            passwordFocusRequester = FocusRequester(),
            organizationFocusRequester = FocusRequester(),
            onTitleRequestFocus = {},
            onPasswordRequestFocus = {},
            onEnterOrganization = {},
            onEnterTitle = {},
            onEnterPassword = {},
            onEnterAdditionalFieldTitle = { _, _ -> },
            onEnterAdditionalFieldValue = { _, _ -> },
            onDeleteAdditionalField = {},
            onAddAdditionalField = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AddEditPasswordPreviewWithAdditionalFieldsDark() {
    ISafeTheme {
        AddEditPasswordContent(
            state = AddEditPasswordViewModel.State(
                organization = "Google",
                title = "Test",
                password = "Pass123$",
                additionalFields = listOf(
                    com.thejohnsondev.model.AdditionalField(
                        id = "1",
                        title = "Test",
                        value = "Test"
                    ),
                )
            ),
            titleFocusRequester = FocusRequester(),
            passwordFocusRequester = FocusRequester(),
            organizationFocusRequester = FocusRequester(),
            onTitleRequestFocus = {},
            onPasswordRequestFocus = {},
            onEnterOrganization = {},
            onEnterTitle = {},
            onEnterPassword = {},
            onEnterAdditionalFieldTitle = { _, _ -> },
            onEnterAdditionalFieldValue = { _, _ -> },
            onDeleteAdditionalField = {},
            onAddAdditionalField = {}
        )
    }
}