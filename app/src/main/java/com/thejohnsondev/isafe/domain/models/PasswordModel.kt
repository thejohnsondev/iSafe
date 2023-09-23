package com.thejohnsondev.isafe.domain.models

data class PasswordModel(
    val timestamp: String,
    val organization: String,
    val organizationLogo: String? = null,
    val title: String,
    val password: String,
    val additionalFields: List<AdditionalField> = emptyList()
)
