package com.thejohnsondev.isafe.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.thejohnsondev.isafe.utils.Size24

@Preview
@Composable
fun UploadFileButton(
    modifier: Modifier = Modifier,
    onUploadClick: () -> Unit = {},
    isLoading: Boolean = true
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
                    modifier = Modifier.padding(Size24),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            } else {
                Image(
                    modifier = Modifier
                        .padding(Size24)
                        .fillMaxSize(),
                    painter = painterResource(id = R.drawable.ic_upload_file),
                    contentDescription = stringResource(R.string.upload_image),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer)
                )
            }
        }
    }
}