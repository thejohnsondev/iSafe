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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.thejohnsondev.designsystem.ISafeTheme
import com.thejohnsondev.ui.ui_model.ButtonShape

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
            Icon(imageVector = icon, contentDescription = "")
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
    ISafeTheme {
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