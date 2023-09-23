package com.thejohnsondev.isafe.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.thejohnsondev.isafe.R
import com.thejohnsondev.isafe.presentation.ui.theme.EqualRounded
import com.thejohnsondev.isafe.utils.Size12
import com.thejohnsondev.isafe.utils.Size8

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AdditionalField(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    onTitleChanged: (String) -> Unit,
    onValueChanged: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val titleFocusRequester = remember {
        FocusRequester()
    }
    val valueFocusRequester = remember {
        FocusRequester()
    }
    var isHidden by remember {
        mutableStateOf(false)
    }
    val eyeImage = if (isHidden) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                isHidden = !isHidden
            },
        shape = EqualRounded.small,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column {
            HintTextField(
                modifier = Modifier
                    .padding(Size12)
                    .fillMaxWidth()
                    .focusRequester(titleFocusRequester),
                value = title,
                onValueChanged = {
                    onTitleChanged(it)
                },
                imeAction = ImeAction.Next,
                onKeyboardAction = {
                    valueFocusRequester.requestFocus()
                },
                hint = stringResource(id = R.string.title)
            )
            Divider()
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top
                ) {
                    HintTextField(
                        modifier = Modifier
                            .padding(Size12)
                            .fillMaxWidth(0.9f)
                            .focusRequester(valueFocusRequester),
                        value = value,
                        onValueChanged = {
                            onValueChanged(it)
                        },
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password,
                        passwordVisible = !isHidden,
                        hint = stringResource(R.string.value),
                        onKeyboardAction = {
                            keyboardController?.hide()
                        }
                    )
                }
                IconButton(
                    onClick = {
                        isHidden = !isHidden
                    }
                ) {
                    Icon(
                        modifier = Modifier.padding(end = Size8),
                        imageVector = eyeImage,
                        contentDescription = stringResource(R.string.visibility),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        titleFocusRequester.requestFocus()
    }
}

@Preview
@Composable
fun Preview() {
    AdditionalField(title = "Title", value = "Value", onTitleChanged = {}, onValueChanged = {})
}