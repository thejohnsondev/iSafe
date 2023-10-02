package com.thejohnsondev.isafe.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.thejohnsondev.isafe.R
import com.thejohnsondev.isafe.presentation.ui.theme.EqualRounded
import com.thejohnsondev.isafe.utils.EMPTY
import com.thejohnsondev.isafe.utils.Percent90
import com.thejohnsondev.isafe.utils.Size16
import com.thejohnsondev.isafe.utils.Size24

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onQueryEntered: (String) -> Unit,
    onQueryClear: () -> Unit
) {
    var searchQuery by remember {
        mutableStateOf("")
    }
    val focusRequester = remember {
        FocusRequester()
    }
    val focusManager = LocalFocusManager.current
    Surface(
        modifier = modifier,
        shape = EqualRounded.large,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .padding(Size16),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HintTextField(
                modifier = Modifier
                    .wrapContentWidth()
                    .fillMaxWidth(Percent90),
                onValueChanged = {
                    searchQuery = it
                    onQueryEntered(searchQuery)
                },
                value = searchQuery,
                hint = if (searchQuery.isBlank()) stringResource(R.string.search) else EMPTY,
                textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusRequester = focusRequester
            )
            if (searchQuery.isNotBlank()) {
                IconButton(
                    modifier = Modifier
                        .size(Size24),
                    onClick = {
                        searchQuery = EMPTY
                        onQueryClear()
                        focusManager.clearFocus()
                    }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                }
            }

        }
    }
}

@Preview
@Composable
fun SearchBarPreview() {
    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(Size16),
        onQueryEntered = {

        },
        onQueryClear = {

        })
}