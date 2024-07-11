package com.thejohnsondev.model

data class ChangePasswordBody(
    val password: String,
    val newPassword: String
)
