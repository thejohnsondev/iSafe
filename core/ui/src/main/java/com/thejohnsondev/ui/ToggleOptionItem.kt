package com.thejohnsondev.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size4

@Composable
fun ToggleOptionItem(
    modifier: Modifier = Modifier,
    optionTitle: String,
    isSelected: Boolean,
    isFirstItem: Boolean = false,
    isLastItem: Boolean = false,
    onOptionToggle: (Boolean) -> Unit = {}
) {
    val haptic = LocalHapticFeedback.current
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(
                RoundedCornerShape(
                    topStart = if (isFirstItem) Size16 else Size4,
                    topEnd = if (isFirstItem) Size16 else Size4,
                    bottomStart = if (isLastItem) Size16 else Size4,
                    bottomEnd = if (isLastItem) Size16 else Size4
                ),
            )
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onOptionToggle(!isSelected)
            },
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = Size16, vertical = Size16),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = optionTitle,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer
            )
            Switch(
                checked = isSelected,
                onCheckedChange = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onOptionToggle(it)
                }
            )
        }

    }

}

@Preview
@Composable
fun ToggleOptionItemPreview() {
    ToggleOptionItem(
        optionTitle = "Option 1",
        isSelected = true,
        isLastItem = true,
        isFirstItem = true
    )
}