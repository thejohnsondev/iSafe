package com.thejohnsondev.domain.add_edit_password

import com.thejohnsondev.domain.vault.DeletePasswordUseCase
import javax.inject.Inject

data class AddEditPasswordUseCases @Inject constructor(
    val createPassword: CreatePasswordUseCase,
    val updatePassword: UpdatePasswordUseCase,
    val deletePassword: DeletePasswordUseCase
)