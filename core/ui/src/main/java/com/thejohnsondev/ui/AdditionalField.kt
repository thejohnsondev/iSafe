package com.thejohnsondev.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.HorizontalDivider
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
import com.thejohnsondev.designsystem.EqualRounded
import com.thejohnsondev.designsystem.Size12
import com.thejohnsondev.designsystem.Size40
import com.thejohnsondev.designsystem.Size64
import com.thejohnsondev.designsystem.Size8

@Composable
fun AdditionalField(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    onTitleChanged: (String) -> Unit,
    onValueChanged: (String) -> Unit,
    onDeleteClick: () -> Unit
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
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
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
                        hint = stringResource(id = com.thejohnsondev.common.R.string.title)
                    )
                    HorizontalDivider()
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Top
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
                                hint = stringResource(com.thejohnsondev.common.R.string.value),
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
                                contentDescription = stringResource(com.thejohnsondev.common.R.string.visibility),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(Size64)
                    .clickable {
                        onDeleteClick()
                    },
                shape = EqualRounded.small,
                color = MaterialTheme.colorScheme.errorContainer
            ) {
                Icon(
                    modifier = Modifier.padding(horizontal = Size8),
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(com.thejohnsondev.common.R.string.visibility),
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
    LaunchedEffect(titleFocusRequester) {
        titleFocusRequester.requestFocus()
    }
}

@Preview
@Composable
fun Preview() {
    AdditionalField(
        title = "Title",
        value = "Value",
        onTitleChanged = {},
        onValueChanged = {},
        onDeleteClick = {})
}