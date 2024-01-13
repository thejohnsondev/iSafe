package com.thejohnsondev.domain

import com.thejohnsondev.data.PasswordsRepository
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.PasswordModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdatePasswordsUseCase @Inject constructor(
    private val passwordRepository: PasswordsRepository
) {
    operator fun invoke(userId: String, newPasswordsList: List<PasswordModel>): Flow<DatabaseResponse> {
        return passwordRepository.updatePasswordsList(userId, newPasswordsList)
    }
}