package com.thejohnsondev.domain

import javax.inject.Inject

data class ToolsUseCases @Inject constructor(
    val getTools: GetToolsUseCase
)
