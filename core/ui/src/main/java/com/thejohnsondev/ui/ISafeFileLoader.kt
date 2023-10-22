package com.thejohnsondev.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.thejohnsondev.designsystem.Size150
import com.thejohnsondev.designsystem.Size16

@Composable
fun ISafeFileLoader(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isError: Boolean = false,
    descriptionText: String = stringResource(com.thejohnsondev.common.R.string.click_to_upload_your_key_file),
    onFilePicked: (Uri?) -> Unit = {}
) {

    val singleFilePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) {
        onFilePicked(it)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        UploadFileButton(
            modifier = Modifier.size(Size150),
            onUploadClick = {
                singleFilePickerLauncher.launch(
                    PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            isLoading = isLoading,
            isError = isError
        )
        Text(
            modifier = Modifier.padding(Size16),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            text = descriptionText,
            textAlign = TextAlign.Center
        )
    }

}


@Preview
@Composable
fun FileLoaderPreview() {
    ISafeFileLoader()
}