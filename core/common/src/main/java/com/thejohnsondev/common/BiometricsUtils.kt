package com.thejohnsondev.common

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

fun Context.isBiometricAvailable(): Boolean {
    val biometricManager = BiometricManager.from(this)
    return when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
        BiometricManager.BIOMETRIC_SUCCESS -> true
        else -> false
    }
}

fun Context.showBiometricPrompt(
    title: String,
    subtitle: String? = null,
    description: String? = null,
    onBiometricSuccess: () -> Unit,
    onBiometricError: ((Int) -> Unit)? = null
) {
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(title)
        .setSubtitle(subtitle)
        .setDescription(description)
        .setConfirmationRequired(false)
        .setNegativeButtonText("Cancel")
        .build()

    val executor = ContextCompat.getMainExecutor(this)

    val biometricPrompt = BiometricPrompt(
        this as FragmentActivity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onBiometricSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onBiometricError?.invoke(errorCode)
            }
        }
    )

    biometricPrompt.authenticate(promptInfo)
}
