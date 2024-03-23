package com.thejohnsondev.ui

import android.annotation.SuppressLint
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.thejohnsondev.common.EXPAND_ANIM_DURATION
import com.thejohnsondev.common.NOTE_TIME_FORMAT
import com.thejohnsondev.common.R
import com.thejohnsondev.common.getTimeFormatted
import com.thejohnsondev.common.hidden
import com.thejohnsondev.designsystem.EqualRounded
import com.thejohnsondev.designsystem.Percent80
import com.thejohnsondev.designsystem.Size12
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size4
import com.thejohnsondev.designsystem.Size42
import com.thejohnsondev.designsystem.Size56
import com.thejohnsondev.designsystem.Size8
import com.thejohnsondev.designsystem.SizeDefault
import com.thejohnsondev.model.AdditionalField
import com.thejohnsondev.model.PasswordModel

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun PasswordItem(
    modifier: Modifier = Modifier,
    item: PasswordModel,
    isReordering: Boolean = false,
    isDragging: Boolean = false,
    onClick: (PasswordModel) -> Unit,
    onCopySensitiveClick: (String) -> Unit,
    onCopyClick: (String) -> Unit,
    onDeleteClick: (PasswordModel) -> Unit,
    onEditClick: (PasswordModel) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var expanded by remember {
        mutableStateOf(false)
    }
    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }
    val transition = updateTransition(transitionState, label = "")
    val cardBgColor by transition.animateColor({
        tween(durationMillis = EXPAND_ANIM_DURATION)
    }, label = "") {
        if (expanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    }
    val draggingCardBgColor by transition.animateColor({
        tween(durationMillis = EXPAND_ANIM_DURATION)
    }, label = "") {
        if (isDragging) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor by transition.animateColor({
        tween(durationMillis = EXPAND_ANIM_DURATION)
    }, label = "") {
        if (expanded) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    }
    val draggingContentColor by transition.animateColor({
        tween(durationMillis = EXPAND_ANIM_DURATION)
    }, label = "") {
        if (isDragging) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    }
    val cardPaddingHorizontal by transition.animateDp({
        tween(durationMillis = EXPAND_ANIM_DURATION)
    }, label = "") {
        if (expanded) Size8 else Size16
    }
    val imageSize by transition.animateDp({
        tween(durationMillis = EXPAND_ANIM_DURATION)
    }, label = "") {
        if (isDragging) Size56 else Size42
    }


    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = cardPaddingHorizontal, bottom = Size8, end = cardPaddingHorizontal),
        shape = EqualRounded.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (isReordering) draggingCardBgColor else cardBgColor
        )
    ) {
        Column(
            modifier = if (isReordering) {
                Modifier
            } else {
                Modifier
                    .combinedClickable(
                        onClick = {
                            onClick(item)
                            expanded = !expanded
                        },
                        onLongClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onCopyClick(item.title)
                        })
            },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .padding(Size16)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(imageSize),
                        color = Color.White,
                        shape = EqualRounded.small
                    ) {
                        LoadedImage(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(Size4),
                            imageUrl = item.organizationLogo ?: "",
                            errorImageVector = com.thejohnsondev.designsystem.R.drawable.ic_passwords,
                            placeholderResId = com.thejohnsondev.designsystem.R.drawable.ic_passwords,
                            backgroundColor = Color.White
                        )
                    }
                    Column {
                        Text(
                            modifier = Modifier
                                .padding(start = Size16)
                                .fillMaxWidth(Percent80),
                            text = item.organization,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isReordering) draggingContentColor else contentColor,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                        Text(
                            modifier = Modifier
                                .padding(start = Size16)
                                .fillMaxWidth(Percent80),
                            text = item.title,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isReordering) draggingContentColor else contentColor,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                }

                IconButton(
                    modifier = Modifier
                        .size(Size42)
                        .bounceClick(),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onCopySensitiveClick(item.password)
                    }) {
                    Icon(
                        imageVector = if (isReordering) Icons.Default.DragHandle else Icons.Default.ContentCopy,
                        contentDescription = stringResource(
                            com.thejohnsondev.common.R.string.copy
                        ),
                        tint = if (isReordering) draggingContentColor else contentColor
                    )
                }

            }
        }

        ExpandableContent(
            visible = expanded
        ) {
            ExpandedContent(
                passwordModel = item,
                onCopyClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onCopySensitiveClick(it)
                },
                onDeleteClick = {
                    expanded = false
                    onDeleteClick(it)
                },
                onEditClick = {
                    onEditClick(it)
                }
            )
        }

    }

}

@Composable
fun ExpandedContent(
    passwordModel: PasswordModel,
    onCopyClick: (String) -> Unit,
    onDeleteClick: (PasswordModel) -> Unit,
    onEditClick: (PasswordModel) -> Unit
) {
    var isHidden by remember {
        mutableStateOf(true)
    }
    val haptic = LocalHapticFeedback.current
    val password = if (isHidden) passwordModel.password.hidden() else passwordModel.password
    val eyeImage = if (isHidden) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = Size16, end = Size16)
                .clickable {
                    isHidden = !isHidden
                },
            shape = EqualRounded.small,
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(Size12), text = password,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Icon(
                    modifier = Modifier.padding(end = Size8),
                    imageVector = eyeImage,
                    contentDescription = stringResource(com.thejohnsondev.common.R.string.visibility)
                )
            }
        }
        if (passwordModel.additionalFields.isNotEmpty()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = Size16, end = Size16, top = Size16),
                shape = EqualRounded.small,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Column {
                    passwordModel.additionalFields.forEach {
                        AdditionalFieldItem(additionalField = it) {
                            onCopyClick(it)
                        }
                    }
                }

            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(start = Size16, end = Size8, bottom = Size8, top = Size16)
                    .bounceClick(),
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onEditClick(passwordModel)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                shape = EqualRounded.medium,
            ) {
                Icon(
                    modifier = Modifier
                        .padding(end = Size4)
                        .size(Size16),
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    modifier = Modifier
                        .padding(vertical = Size4),
                    text = stringResource(com.thejohnsondev.common.R.string.edit),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Button(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(start = Size8, end = Size16, bottom = Size8, top = Size16)
                    .bounceClick(),
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onDeleteClick(passwordModel)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                shape = EqualRounded.medium
            ) {
                Icon(
                    modifier = Modifier
                        .padding(end = Size4)
                        .size(Size16),
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
                Text(
                    modifier = Modifier
                        .padding(vertical = Size4),
                    text = stringResource(id = com.thejohnsondev.common.R.string.delete),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
                .padding(start = Size16, end = Size16, bottom = Size16),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            text = stringResource(
                id = R.string.last_edit,
                passwordModel.lastEdit.toLongOrNull()?.getTimeFormatted(NOTE_TIME_FORMAT).orEmpty()
            )
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AdditionalFieldItem(
    additionalField: AdditionalField,
    onLongClick: (String) -> Unit
) {
    var isHidden by remember {
        mutableStateOf(true)
    }
    val value = if (isHidden) additionalField.value.hidden() else additionalField.value
    val eyeImage = if (isHidden) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    isHidden = !isHidden
                },
                onLongClick = {
                    onLongClick(additionalField.value)
                }
            ),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = Size8, end = Size8, top = Size12, bottom = Size4),
                    text = additionalField.title
                )
                Text(
                    modifier = Modifier
                        .padding(start = Size8, end = Size8, top = Size4, bottom = Size12),
                    text = value
                )
            }
            Icon(
                modifier = Modifier.padding(end = Size8),
                imageVector = eyeImage,
                contentDescription = stringResource(com.thejohnsondev.common.R.string.visibility)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PasswordItemPreview() {
    PasswordItem(item = PasswordModel(
        id = "1694854940885",
        organization = "Google Google Google Google Google Google",
        title = "emal@gmail.com emal@gmail.com emal@gmail.com emal@gmail.com emal@gmail.com",
        password = "Pass123$",
        lastEdit = "1711195873"
    ),
        isReordering = false,
        onClick = {},
        onCopySensitiveClick = {},
        onCopyClick = {},
        onDeleteClick = {},
        onEditClick = {})
}

@Preview(showBackground = true)
@Composable
fun PasswordItemPreviewReorder() {
    PasswordItem(item = PasswordModel(
        id = "1694854940885",
        organization = "Google Google Google Google Google Google",
        title = "emal@gmail.com emal@gmail.com emal@gmail.com emal@gmail.com emal@gmail.com",
        password = "Pass123$",
        lastEdit = "1711195873"
    ),
        isReordering = true,
        onClick = {},
        onCopySensitiveClick = {},
        onCopyClick = {},
        onDeleteClick = {},
        onEditClick = {})
}

@Preview(showBackground = true)
@Composable
fun PasswordItemPreviewDragging() {
    PasswordItem(item = PasswordModel(
        id = "1694854940885",
        organization = "Google Google Google Google Google Google",
        title = "emal@gmail.com emal@gmail.com emal@gmail.com emal@gmail.com emal@gmail.com",
        password = "Pass123$",
        lastEdit = "1711195873"
    ),
        isReordering = true,
        isDragging = true,
        onClick = {},
        onCopySensitiveClick = {},
        onCopyClick = {},
        onDeleteClick = {},
        onEditClick = {})
}

@Preview(showBackground = true, widthDp = 500)
@Composable
fun PasswordItemPreview2() {
    PasswordItem(item = PasswordModel(
        id = "1694854940885",
        organization = "Google Google Google Google Google Google",
        title = "emal@gmail.com emal@gmail.com emal@gmail.com emal@gmail.com emal@gmail.com",
        password = "Pass123$",
        lastEdit = "1711195873"
    ),
        isReordering = false,
        onClick = {},
        onCopySensitiveClick = {},
        onCopyClick = {},
        onDeleteClick = {},
        onEditClick = {})
}

@Preview(showBackground = true, widthDp = 300)
@Composable
fun PasswordItemPreview3() {
    PasswordItem(item = PasswordModel(
        id = "1694854940885",
        organization = "Google Google Google Google Google Google",
        title = "emal@gmail.com emal@gmail.com emal@gmail.com emal@gmail.com emal@gmail.com",
        password = "Pass123$",
        lastEdit = "1711195873"
    ),
        isReordering = false,
        onClick = {},
        onCopySensitiveClick = {},
        onCopyClick = {},
        onDeleteClick = {},
        onEditClick = {})
}

@Preview
@Composable
fun ExpandedContentPreview() {
    ExpandedContent(
        passwordModel = PasswordModel(
            id = "1694854940885",
            organization = "Google",
            title = "emal@gmail.com",
            password = "Pass123$",
            lastEdit = "1711195873"
        ), onDeleteClick = {

        },
        onCopyClick = {

        },
        onEditClick = {

        }
    )
}

@Preview
@Composable
fun AdditionalFieldPreview() {
    AdditionalFieldItem(
        additionalField = AdditionalField(
            "0",
            "Username",
            "Andrew"
        ),
        onLongClick = {

        }
    )
}