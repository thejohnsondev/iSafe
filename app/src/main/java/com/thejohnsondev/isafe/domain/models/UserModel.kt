package com.thejohnsondev.isafe.domain.models

data class UserModel(
    val id: String?,
    val name: String?,
    val userSecret: String? = null
)
