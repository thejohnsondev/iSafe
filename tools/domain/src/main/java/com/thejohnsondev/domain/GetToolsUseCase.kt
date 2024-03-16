package com.thejohnsondev.domain

import arrow.core.Either
import com.thejohnsondev.data.ToolsRepository
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.tools.ToolModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetToolsUseCase @Inject constructor(
    private val toolsRepository: ToolsRepository
) {

    operator fun invoke(): Flow<Either<ApiError, List<ToolModel>>> = toolsRepository.getTools()

}