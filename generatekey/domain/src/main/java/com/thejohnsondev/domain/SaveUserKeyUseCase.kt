package com.thejohnsondev.domain

interface SaveUserKeyUseCase {
    suspend operator fun invoke(userKey: ByteArray)
}