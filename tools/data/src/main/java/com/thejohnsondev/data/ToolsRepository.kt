package com.thejohnsondev.data

import arrow.core.Either
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.tools.ToolModel
import kotlinx.coroutines.flow.Flow

interface ToolsRepository {
    fun getTools(): Flow<Either<ApiError, List<ToolModel>>>
}