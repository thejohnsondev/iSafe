package com.thejohnsondev.isafe.presentation.screens.passwords.list

import com.thejohnsondev.isafe.domain.models.BankAccountModel
import com.thejohnsondev.isafe.domain.models.LoadingState
import com.thejohnsondev.isafe.domain.models.PasswordModel

data class VaultState(
    val loadingState: LoadingState = LoadingState.Loaded,
    val passwordsList: List<PasswordModel> = emptyList(),
    val bankAccountsList: List<BankAccountModel> = emptyList(),
    val isSearching: Boolean = false
)