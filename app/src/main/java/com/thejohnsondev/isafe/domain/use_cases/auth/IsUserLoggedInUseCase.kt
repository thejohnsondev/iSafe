package com.thejohnsondev.isafe.domain.use_cases.auth

interface IsUserLoggedInUseCase {
    operator fun invoke(): Boolean
}