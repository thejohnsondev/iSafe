package com.thejohnsondev.isafe.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.thejohnsondev.isafe.utils.EMPTY
import com.thejohnsondev.isafe.utils.Size16
import com.thejohnsondev.isafe.utils.Size8


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField(
    modifier: Modifier = Modifier,
    textState: MutableState<String>,
    onTextChanged: (String) -> Unit,
    label: String = EMPTY,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    singleLine: Boolean = true,
    onKeyboardAction: KeyboardActions = KeyboardActions.Default,
    errorText: String? = null,
    isError: Boolean = false
) {
    val visualTransformation =
        if (keyboardType != KeyboardType.Password) VisualTransformation.None else PasswordVisualTransformation()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Size16, vertical = Size8)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = textState.value,
            onValueChange = {
                onTextChanged(it)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            label = {
                Text(text = label)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            singleLine = singleLine,
            keyboardActions = onKeyboardAction,
            isError = isError && errorText != null,
            visualTransformation = visualTransformation
        )
        if (isError && errorText != null) {
            Text(
                text = errorText,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = Size16)
            )
        }

    }
}