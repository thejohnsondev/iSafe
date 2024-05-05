package com.thejohnsondev.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size4

@Composable
fun SelectableOptionItem(
    modifier: Modifier = Modifier,
    optionTitle: String,
    isSelected: Boolean,
    isFirstItem: Boolean = false,
    isLastItem: Boolean = false,
    onOptionSelect: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(
                topStart = if (isFirstItem) Size16 else Size4,
                topEnd = if (isFirstItem) Size16 else Size4,
                bottomStart = if (isLastItem) Size16 else Size4,
                bottomEnd = if (isLastItem) Size16 else Size4
            ))
            .clickable {
                onOptionSelect()
            },
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
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
            if (isSelected) {
                Icon(imageVector = Icons.Default.Done, contentDescription = null)
            }
        }
    }
}

@Preview
@Composable
private fun OptionSelectedPreview() {
    SelectableOptionItem(optionTitle = "Option", isSelected = true)
}

@Preview
@Composable
private fun OptionUnselectedPreview() {
    SelectableOptionItem(optionTitle = "Option", isSelected = false)
}