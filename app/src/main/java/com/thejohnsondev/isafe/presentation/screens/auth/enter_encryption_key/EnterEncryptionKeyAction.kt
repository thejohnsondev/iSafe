package com.thejohnsondev.isafe.presentation.screens.auth.enter_encryption_key

import android.net.Uri

sealed class EnterEncryptionKeyAction {
    class GenerateKey(val fileUri: Uri?) : EnterEncryptionKeyAction()
    object Logout : EnterEncryptionKeyAction()
}