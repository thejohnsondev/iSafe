package com.thejohnsondev.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.thejohnsondev.designsystem.AppTheme

@Composable
fun ConfirmAlertDialog(
    icon: ImageVector = Icons.Default.Warning,
    title: String,
    message: String,
    confirmButtonText: String,
    cancelButtonText: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        icon = {
            Icon(imageVector = icon, contentDescription = stringResource(com.thejohnsondev.common.R.string.alert_icon))
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = message)
        },
        onDismissRequest = {
            onCancel()
        },
        confirmButton = {
            RoundedButton(
                text = confirmButtonText,
                onClick = {
                    onConfirm()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            )
        },
        dismissButton = {
            RoundedButton(
                text = cancelButtonText,
                onClick = {
                    onCancel()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    )
}

@PreviewLightDark
@Composable
private fun ConfirmAlertPreview() {
    AppTheme {
        ConfirmAlertDialog(
            icon = Icons.Default.Warning,
            title = "Delete Account",
            message = "Are you sure you want to delete your account?",
            confirmButtonText = "Delete Account",
            cancelButtonText = "Cancel",
            onConfirm = {},
            onCancel = {}
        )
    }
}