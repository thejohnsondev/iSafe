package com.thejohnsondev.isafe.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.thejohnsondev.isafe.utils.Size12
import com.thejohnsondev.isafe.utils.Size16
import com.thejohnsondev.isafe.utils.Size32
import com.thejohnsondev.isafe.utils.Size48
import com.thejohnsondev.isafe.utils.Size8

@Composable
fun FilterGroup(
    filters: List<String>,
    onFilterClick: (String) -> Unit,
    defaultSelected: String
) {
    val selectedFilter = remember {
        mutableStateOf(defaultSelected)
    }

    Row(
        modifier = Modifier
            .padding(horizontal = Size8)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        filters.forEach {
            Chip(title = it, selected = selectedFilter.value) {
                selectedFilter.value = it
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
        .height(Size48)
        .clip(CircleShape)
        .background(background)
        .clickable {
            onSelected(title)
        }) {

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(modifier = Modifier.padding(vertical = Size12, horizontal = if (isSelected) Size32 else Size16), text = title, color = contentColor)
        }

    }
}


