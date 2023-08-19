package com.thejohnsondev.isafe.domain.use_cases.user

interface SaveUserKeyUseCase {
    suspend operator fun invoke(userKey: ByteArray)
}