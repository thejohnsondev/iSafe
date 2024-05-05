package com.thejohnsondev.presentation.usecases

import com.thejohnsondev.domain.DeletePasswordUseCase
import com.thejohnsondev.domain.GetAllPasswordsUseCase
import com.thejohnsondev.domain.GetSettingsConfigUseCase
import com.thejohnsondev.domain.UpdatePasswordsUseCase
import javax.inject.Inject

class VaultUseCases @Inject constructor(
    val getAllPasswords: GetAllPasswordsUseCase,
    val deletePassword: DeletePasswordUseCase,
    val updatePasswordsUseCase: UpdatePasswordsUseCase,
    val getSettingsConfig: GetSettingsConfigUseCase,
)