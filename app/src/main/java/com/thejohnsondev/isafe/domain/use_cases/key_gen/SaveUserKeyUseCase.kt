package com.thejohnsondev.isafe.domain.use_cases.key_gen

interface SaveUserKeyUseCase {
    suspend operator fun invoke(userKey: ByteArray)
}