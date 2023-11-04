package com.thejohnsondev.domain

import javax.inject.Inject

class AddEditPasswordUseCases @Inject constructor(
    val createPassword: CreatePasswordUseCase,
    val updatePassword: UpdatePasswordUseCase,
)