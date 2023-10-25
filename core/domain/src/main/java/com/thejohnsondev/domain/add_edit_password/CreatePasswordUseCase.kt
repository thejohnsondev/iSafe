package com.thejohnsondev.domain.add_edit_password

import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.PasswordModel
import kotlinx.coroutines.flow.Flow

interface CreatePasswordUseCase {
    suspend operator fun invoke(userId: String, password: PasswordModel): Flow<DatabaseResponse>
}