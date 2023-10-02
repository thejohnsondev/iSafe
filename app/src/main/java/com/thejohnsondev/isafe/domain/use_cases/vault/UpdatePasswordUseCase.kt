package com.thejohnsondev.isafe.domain.use_cases.vault

import com.thejohnsondev.isafe.domain.models.DatabaseResponse
import com.thejohnsondev.isafe.domain.models.PasswordModel
import kotlinx.coroutines.flow.Flow

interface UpdatePasswordUseCase {
    suspend operator fun invoke(userId: String, password: PasswordModel): Flow<DatabaseResponse>
}