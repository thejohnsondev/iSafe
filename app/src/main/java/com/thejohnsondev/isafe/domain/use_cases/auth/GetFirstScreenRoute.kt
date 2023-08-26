package com.thejohnsondev.isafe.domain.use_cases.auth

interface GetFirstScreenRoute {
    suspend operator fun invoke(): String
}