package com.thejohnsondev.isafe.presentation.screens.auth.create_encryption_key

import android.net.Uri

sealed class CreateEncryptionKeyAction {
    class GenerateKey(val fileUri: Uri?): CreateEncryptionKeyAction()
    object Logout: CreateEncryptionKeyAction()
}