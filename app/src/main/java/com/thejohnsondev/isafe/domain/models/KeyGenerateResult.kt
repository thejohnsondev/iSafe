package com.thejohnsondev.isafe.domain.models

sealed class KeyGenerateResult {
    class Success(val key: ByteArray): KeyGenerateResult()
    class Failure(val exception: Exception): KeyGenerateResult()
}