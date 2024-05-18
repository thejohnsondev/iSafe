package com.thejohnsondev.presentation.biometric

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import com.thejohnsondev.common.R
import com.thejohnsondev.common.showBiometricPrompt
import com.thejohnsondev.designsystem.ISafeTheme
import com.thejohnsondev.designsystem.Size128
import com.thejohnsondev.designsystem.Size16

@Composable
fun BiometricScreen(
    parentActivity: FragmentActivity,
    onBiometricSuccess: () -> Unit,
) {
    val scanTitle = stringResource(id = R.string.scan_to_unlock)
    LaunchedEffect(key1 = Unit) {
        showBiometricPrompt(
            parentActivity = parentActivity,
            title = scanTitle,
            onBiometricSuccess = onBiometricSuccess
        )
    }
    BiometricScreenContent(
        showPrompt = {
            showBiometricPrompt(
                parentActivity = parentActivity,
                title = scanTitle,
                onBiometricSuccess = onBiometricSuccess
            )
        }
    )
}

fun showBiometricPrompt(
    parentActivity: FragmentActivity,
    title: String,
    onBiometricSuccess: () -> Unit,
) {
    parentActivity.showBiometricPrompt(
        title = title,
        onBiometricSuccess = onBiometricSuccess,
    )
}

@Composable
fun BiometricScreenContent(
    showPrompt: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(start = Size16, end = Size16, top = Size128),
                text = stringResource(id = R.string.biometric_authentication),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                modifier = Modifier.padding(top = Size16),
                text = stringResource(id = R.string.biometric_authentication_description)
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                modifier = Modifier.size(Size128),
                onClick = {
                    showPrompt()
                }) {
                Icon(
                    modifier = Modifier.size(Size128),
                    imageVector = Icons.Default.Fingerprint,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

        }
    }
}

@Preview
@Composable
private fun BiometricScreenContentPreview() {
    ISafeTheme {
        BiometricScreenContent({})
    }
}