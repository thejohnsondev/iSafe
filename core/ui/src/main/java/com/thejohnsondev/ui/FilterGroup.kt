package com.thejohnsondev.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size24
import com.thejohnsondev.designsystem.Size8
import com.thejohnsondev.designsystem.Text14

@Composable
fun FilterGroup(
    filters: List<String>,
    onFilterClick: (String) -> Unit,
    defaultSelected: String
) {
    val selectedFilter = remember {
        mutableStateOf(defaultSelected)
    }

    LazyRow(
        modifier = Modifier
            .padding(horizontal = Size8)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Start
    ) {
        items(filters) {
            Chip(title = it, selected = selectedFilter.value) {
                selectedFilter.value = it
                onFilterClick(it)
            }
        }
    }

}

@Composable
fun Chip(
    title: String,
    selected: String,
    onSelected: (String) -> Unit
) {

    val isSelected = selected == title

    val background =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
    val contentColor =
        if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer

    Box(modifier = Modifier
        .padding(horizontal = Size8, vertical = Size8)
        .wrapContentHeight()
        .clip(CircleShape)
        .background(background)
        .clickable {
            onSelected(title)
        }) {
            Text(
                modifier = Modifier.padding(
                    vertical = Size8, horizontal = if (isSelected) Size24 else Size16
                ),
                text = title,
                color = contentColor,
                fontSize = Text14,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
    }
}

@PreviewLightDark
@Composable
fun FilterGroupPreview() {
    FilterGroup(filters = listOf(
        "All",
        "Active",
        "Inactive"
    ), onFilterClick = {}, defaultSelected = "All"
    )
}

