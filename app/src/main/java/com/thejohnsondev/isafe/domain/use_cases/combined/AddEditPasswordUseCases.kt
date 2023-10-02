package com.thejohnsondev.isafe.domain.use_cases.combined

import com.thejohnsondev.isafe.domain.use_cases.vault.CreatePasswordUseCase
import com.thejohnsondev.isafe.domain.use_cases.vault.DeletePasswordUseCase
import com.thejohnsondev.isafe.domain.use_cases.vault.UpdatePasswordUseCase
import javax.inject.Inject

data class AddEditPasswordUseCases @Inject constructor(
    val createPassword: CreatePasswordUseCase,
    val updatePassword: UpdatePasswordUseCase,
    val deletePassword: DeletePasswordUseCase
)