package com.thejohnsondev.isafe.domain.use_cases.notes

import com.thejohnsondev.isafe.domain.models.UserNotesResponse
import com.thejohnsondev.isafe.domain.repositories.NotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNotesUseCaseImpl @Inject constructor(
    private val notesRepository: NotesRepository
): GetAllNotesUseCase {
    override suspend fun invoke(userId: String): Flow<UserNotesResponse> {
        return notesRepository.getUserNotes(userId)
    }
}