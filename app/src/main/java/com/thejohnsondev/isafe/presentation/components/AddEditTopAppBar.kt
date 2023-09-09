package com.thejohnsondev.isafe.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.thejohnsondev.isafe.R
import com.thejohnsondev.isafe.utils.Size16

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTopAppBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    onNavigateBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: (() -> Unit)? = null
) {
    TopAppBar(
        modifier = modifier,
        title = {
            title?.let {
                Text(text = it)
            }
        },
        actions = {
            Button(
                modifier = Modifier.padding(horizontal = Size16),
                onClick = { onSaveClick() }) {
                Text(text = stringResource(R.string.save))
            }
            onDeleteClick?.let {
                IconButton(
                    modifier = Modifier.padding(end = Size16),
                    onClick = { it() }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete)
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = { onNavigateBackClick() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.arrow_back)
                )
            }
        })

}


@Preview
@Composable
fun AddTopAppBarPreview() {
    AddEditTopAppBar(
        title = "Add note",
        onNavigateBackClick = {},
        onSaveClick = {},
        onDeleteClick = {}
    )
}