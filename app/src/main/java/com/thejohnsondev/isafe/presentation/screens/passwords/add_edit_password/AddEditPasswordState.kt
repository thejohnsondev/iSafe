package com.thejohnsondev.isafe.presentation.screens.passwords.add_edit_password

import com.thejohnsondev.isafe.domain.models.AdditionalField
import com.thejohnsondev.isafe.domain.models.LoadingState

data class AddEditPasswordState(
    val loadingState: LoadingState = LoadingState.Loaded,
    val organization: String = "",
    val title: String = "",
    val password: String = "",
    val additionalFields: List<AdditionalField> = emptyList(),
    val timeStamp: String = "",
    val isEdit: Boolean = false
)