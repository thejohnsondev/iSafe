package com.thejohnsondev.isafe.domain.use_cases.combined

import com.thejohnsondev.isafe.domain.use_cases.vault.DeletePasswordUseCase
import com.thejohnsondev.isafe.domain.use_cases.vault.GetAllPasswordsUseCase
import javax.inject.Inject

class VaultUseCases @Inject constructor(
    val getAllPasswords: GetAllPasswordsUseCase,
    val deletePassword: DeletePasswordUseCase
)