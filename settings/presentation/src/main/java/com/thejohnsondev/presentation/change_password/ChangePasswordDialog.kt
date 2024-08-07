package com.thejohnsondev.presentation.change_password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.thejohnsondev.common.EMPTY
import com.thejohnsondev.common.getPasswordErrorMessage
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size8
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.PasswordValidationState
import com.thejohnsondev.presentation.SettingsViewModel
import com.thejohnsondev.ui.RoundedButton
import com.thejohnsondev.ui.TextField
import com.thejohnsondev.ui.utils.keyboardAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordDialog(
    onAction: (SettingsViewModel.Action) -> Unit,
    settingsScreenState: SettingsViewModel.State,
    onGoBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val oldPassword = rememberSaveable {
        mutableStateOf(EMPTY)
    }
    val newPassword = rememberSaveable {
        mutableStateOf(EMPTY)
    }
    val confirmPassword = rememberSaveable {
        mutableStateOf(EMPTY)
    }
    val oldPasswordFocusRequest = remember { FocusRequester() }
    val newPasswordFocusRequest = remember { FocusRequester() }
    val confirmPasswordFocusRequest = remember { FocusRequester() }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    val isKeyboardOpened by keyboardAsState()

    val isConfirmButtonActive =
        settingsScreenState.isConfirmPasswordMatches == true && settingsScreenState.newPasswordValidationState == PasswordValidationState.PasswordCorrectState


    ModalBottomSheet(
        onDismissRequest = {
            onGoBackClick()
        },
        sheetState = sheetState
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(if (isKeyboardOpened) 1f else 0.8f),
            color = AlertDialogDefaults.containerColor,
            shape = AlertDialogDefaults.shape,
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Size16)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                ) {
                    Text(
                        text = stringResource(com.thejohnsondev.common.R.string.change_password),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        modifier = Modifier.padding(top = Size16),
                        text = stringResource(com.thejohnsondev.common.R.string.please_enter_your_old_password_and_a_new_password),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                    TextField(
                        modifier = Modifier
                            .focusRequester(oldPasswordFocusRequest)
                            .padding(start = Size8, end = Size8, top = Size16),
                        textState = oldPassword,
                        onTextChanged = {
                            oldPassword.value = it
                        },
                        label = stringResource(com.thejohnsondev.common.R.string.old_password),
                        onKeyboardAction = KeyboardActions {
                            newPasswordFocusRequest.requestFocus()
                        },
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Password
                    )
                    TextField(
                        modifier = Modifier
                            .focusRequester(newPasswordFocusRequest)
                            .padding(start = Size8, end = Size8, top = Size16),
                        textState = newPassword,
                        onTextChanged = {
                            onAction(SettingsViewModel.Action.ValidateNewPassword(it, confirmPassword.value))
                            newPassword.value = it
                        },
                        label = stringResource(com.thejohnsondev.common.R.string.new_password),
                        onKeyboardAction = KeyboardActions {
                            confirmPasswordFocusRequest.requestFocus()
                        },
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Password,
                        isError = settingsScreenState.newPasswordValidationState != PasswordValidationState.PasswordCorrectState,
                        errorText = if (settingsScreenState.newPasswordValidationState is PasswordValidationState.PasswordIncorrectState) context.getPasswordErrorMessage(
                            (settingsScreenState.newPasswordValidationState as PasswordValidationState.PasswordIncorrectState).reason
                        ) else null
                    )
                    TextField(
                        modifier = Modifier
                            .focusRequester(confirmPasswordFocusRequest)
                            .padding(start = Size8, end = Size8, top = Size16),
                        textState = confirmPassword,
                        onTextChanged = {
                            onAction(
                                SettingsViewModel.Action.EnterConfirmPassword(
                                    it,
                                    newPassword.value
                                )
                            )
                            confirmPassword.value = it
                        },
                        label = stringResource(com.thejohnsondev.common.R.string.confirm_password),
                        onKeyboardAction = KeyboardActions {
                            if (isConfirmButtonActive) {
                                onAction(
                                    SettingsViewModel.Action.ChangePassword(
                                        oldPassword = oldPassword.value,
                                        newPassword = newPassword.value
                                    )
                                )
                            }
                        },
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password,
                        isError = settingsScreenState.isConfirmPasswordMatches?.not() ?: false,
                        errorText = if (settingsScreenState.isConfirmPasswordMatches?.not() == true) stringResource(
                            com.thejohnsondev.common.R.string.password_don_t_match
                        ) else null
                    )
                }
                RoundedButton(
                    modifier = Modifier
                        .padding(Size16)
                        .align(Alignment.BottomCenter),
                    text = stringResource(com.thejohnsondev.common.R.string.confirm),
                    onClick = {
                        onAction(
                            SettingsViewModel.Action.ChangePassword(
                                oldPassword = oldPassword.value,
                                newPassword = newPassword.value
                            )
                        )
                    },
                    loading = settingsScreenState.updatePasswordLoadingState == LoadingState.Loading,
                    enabled = settingsScreenState.newPasswordValidationState == PasswordValidationState.PasswordCorrectState && settingsScreenState.isConfirmPasswordMatches == true,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        }
    }

}