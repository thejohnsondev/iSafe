package com.thejohnsondev.isafe.domain.models

data class BankAccountModel(
    val timestamp: String,
    val userName: String,
    val cardNumber: String,
    val expirationDateTimeStamp: String,
    val cvv: String,
    val pin: String
)