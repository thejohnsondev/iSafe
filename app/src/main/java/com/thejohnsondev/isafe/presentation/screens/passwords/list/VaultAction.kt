package com.thejohnsondev.isafe.presentation.screens.passwords.list

import com.thejohnsondev.isafe.domain.models.PasswordModel

sealed class VaultAction {
    object FetchVault: VaultAction()
    class DeletePassword(val password: PasswordModel): VaultAction()
    class Search(val query: String): VaultAction()
    object StopSearching: VaultAction()
}