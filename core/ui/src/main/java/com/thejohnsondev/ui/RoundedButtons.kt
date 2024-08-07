package com.thejohnsondev.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.thejohnsondev.designsystem.ISafeTheme
import com.thejohnsondev.designsystem.Percent70
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size24
import com.thejohnsondev.designsystem.Size4
import com.thejohnsondev.designsystem.Size48
import com.thejohnsondev.designsystem.Size8
import com.thejohnsondev.ui.ui_model.ButtonShape

@Composable
fun RoundedButton(
    modifier: Modifier = Modifier,
    text: String = stringResource(com.thejohnsondev.common.R.string.buttons),
    imageVector: ImageVector? = null,
    loading: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ),
    buttonShape: ButtonShape = ButtonShape.ROUNDED
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(Size48)
            .clip(
                RoundedCornerShape(
                    topStart = buttonShape.topStart,
                    topEnd = buttonShape.topEnd,
                    bottomStart = buttonShape.bottomStart,
                    bottomEnd = buttonShape.bottomEnd
                )
            )
            .clickable {
                if (enabled && !loading) {
                    onClick()
                }
            },
        color = if (enabled && !loading) colors.containerColor else colors.containerColor.copy(alpha = Percent70),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = Size16),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (loading) {
                ISafeLoading(
                    modifier = Modifier.size(Size24),
                    iconTintColor = if (enabled) colors.contentColor else colors.contentColor.copy(
                        alpha = Percent70
                    )
                )
            } else {
                imageVector?.let { safeImageVector ->
                    Icon(
                        modifier = Modifier
                            .padding(end = Size4)
                            .size(Size16),
                        imageVector = safeImageVector,
                        contentDescription = stringResource(com.thejohnsondev.common.R.string.app_logo),
                        tint = if (enabled) colors.contentColor else colors.contentColor.copy(
                            alpha = Percent70
                        )
                    )
                }
                Text(
                    text = text,
                    color = if (enabled) colors.contentColor else colors.contentColor.copy(
                        alpha = Percent70
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
            }

        }
    }
}

@Preview
@Composable
private fun RoundedButtonPreview() {
    ISafeTheme {
        RoundedButton(
            modifier = Modifier
                .height(Size48),
            text = "Button",
            imageVector = Icons.Default.Edit,
            loading = false,
            enabled = true,
        )
    }
}

@Preview
@Composable
private fun RectangleButtonPreview() {
    ISafeTheme {
        RoundedButton(
            text = "Button",
            loading = false,
            enabled = true,
            buttonShape = ButtonShape.RECTANGLE
        )
    }
}

@Preview
@Composable
private fun TopRoundedButtonPreview() {
    ISafeTheme {
        RoundedButton(
            text = "Button",
            loading = false,
            enabled = true,
            buttonShape = ButtonShape.TOP_ROUNDED
        )
    }
}

@Preview
@Composable
private fun BottomRoundedButtonPreview() {
    ISafeTheme {
        RoundedButton(
            text = "Button",
            loading = false,
            enabled = true,
            buttonShape = ButtonShape.BOTTOM_ROUNDED
        )
    }
}

@Preview
@Composable
private fun StartRoundedButtonPreview() {
    ISafeTheme {
        RoundedButton(
            text = "Button",
            loading = false,
            enabled = true,
            buttonShape = ButtonShape.START_ROUNDED
        )
    }
}

@Preview
@Composable
private fun EndRoundedButtonPreview() {
    ISafeTheme {
        RoundedButton(
            text = "Button",
            loading = false,
            enabled = true,
            buttonShape = ButtonShape.END_ROUNDED
        )
    }
}

@Preview
@Composable
private fun VerticalButtonsGroupPreview() {
    ISafeTheme {
        Column {
            RoundedButton(
                modifier = Modifier
                    .padding(Size8),
                text = "Button",
                loading = false,
                enabled = false,
                buttonShape = ButtonShape.TOP_ROUNDED,
            )
            RoundedButton(
                modifier = Modifier
                    .padding(Size8),
                text = "Button",
                loading = true,
                enabled = true,
                buttonShape = ButtonShape.RECTANGLE
            )
            RoundedButton(
                modifier = Modifier
                    .padding(Size8),
                text = "Button",
                loading = false,
                enabled = true,
                buttonShape = ButtonShape.BOTTOM_ROUNDED
            )
        }
    }
}

@Preview
@Composable
private fun HorizontalButtonsGroupPreview() {
    ISafeTheme {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RoundedButton(
                modifier = Modifier
                    .padding(Size8),
                text = "Button",
                loading = false,
                enabled = true,
                buttonShape = ButtonShape.START_ROUNDED
            )
            RoundedButton(
                modifier = Modifier
                    .padding(Size8),
                text = "Button",
                loading = false,
                enabled = true,
                buttonShape = ButtonShape.END_ROUNDED
            )
        }
    }
}