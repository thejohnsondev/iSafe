package com.thejohnsondev.isafe.domain.use_cases.user

interface GetLocalUserSecretUseCase {
    suspend operator fun invoke(): String?
}