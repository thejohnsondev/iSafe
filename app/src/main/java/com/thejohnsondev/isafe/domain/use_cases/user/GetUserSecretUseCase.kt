package com.thejohnsondev.isafe.domain.use_cases.user

interface GetUserSecretUseCase {
    suspend operator fun invoke(): String?
}