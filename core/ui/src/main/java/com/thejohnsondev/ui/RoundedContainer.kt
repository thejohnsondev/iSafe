package com.thejohnsondev.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.thejohnsondev.designsystem.AppTheme
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size4

@Composable
fun RoundedContainer(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    isFirstItem: Boolean = false,
    isLastItem: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(
            topStart = if (isFirstItem) Size16 else Size4,
            topEnd = if (isFirstItem) Size16 else Size4,
            bottomStart = if (isLastItem) Size16 else Size4,
            bottomEnd = if (isLastItem) Size16 else Size4
        ),
        color = color
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            content(this)
        }
    }
}

@Preview
@Composable
private fun RoundedContainerTopPreview() {
    AppTheme {
        RoundedContainer(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = Size16, end = Size16),
            isFirstItem = true
        ) {
            Text(modifier = Modifier.padding(Size16), text = "RoundedContainer")
        }
    }
}

@Preview
@Composable
private fun RoundedContainerBottomPreview() {
    AppTheme {
        RoundedContainer(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = Size16, end = Size16),
            isLastItem = true
        ) {
            Text(modifier = Modifier.padding(Size16), text = "RoundedContainer")
        }
    }
}