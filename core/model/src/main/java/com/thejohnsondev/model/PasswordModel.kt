package com.thejohnsondev.model

data class PasswordModel(
    val id: String? = null,
    val organization: String,
    val organizationLogo: String? = null,
    val title: String,
    val password: String,
    val lastEdit: String,
    val additionalFields: List<AdditionalField> = emptyList()
)
