package com.thejohnsondev.domain.vault

import javax.inject.Inject

class VaultUseCases @Inject constructor(
    val getAllPasswords: GetAllPasswordsUseCase,
    val deletePassword: DeletePasswordUseCase
)