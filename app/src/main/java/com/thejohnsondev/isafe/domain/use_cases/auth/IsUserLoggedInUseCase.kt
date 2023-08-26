package com.thejohnsondev.isafe.domain.use_cases.auth

interface IsUserLoggedInUseCase {
    suspend operator fun invoke(): Boolean
}