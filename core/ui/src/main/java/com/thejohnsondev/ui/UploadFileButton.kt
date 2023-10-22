package com.thejohnsondev.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.thejohnsondev.isafe.R
import com.thejohnsondev.isafe.presentation.ui.theme.EqualRounded
import com.thejohnsondev.isafe.utils.Size150
import com.thejohnsondev.isafe.utils.Size24

@Composable
fun UploadFileButton(
    modifier: Modifier = Modifier,
    onUploadClick: () -> Unit = {},
    isLoading: Boolean = false,
    isError: Boolean = false
) {
    Surface(
        shape = EqualRounded.large,
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier,
    ) {
        Column(modifier = Modifier.clickable {
            onUploadClick()
        }) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(Size24)
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                return@Column
            }
            Image(
                modifier = Modifier
                    .padding(Size24)
                    .fillMaxSize(),
                painter = painterResource(id = if (isError) R.drawable.ic_error else R.drawable.ic_upload_file),
                contentDescription = stringResource(if (isError) R.string.upload_file_error else R.string.upload_image),
                colorFilter = ColorFilter.tint(if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onPrimaryContainer)
            )
        }
    }
}

@Preview
@Composable
fun InitialStatePreview() {
    UploadFileButton(
        modifier = Modifier.size(Size150)
    )
}

@Preview
@Composable
fun LoadingStatePreview() {
    UploadFileButton(
        modifier = Modifier.size(Size150),
        isLoading = true
    )
}

@Preview
@Composable
fun ErrorStatePreview() {
    UploadFileButton(
        modifier = Modifier.size(Size150),
        isError = true
    )
}