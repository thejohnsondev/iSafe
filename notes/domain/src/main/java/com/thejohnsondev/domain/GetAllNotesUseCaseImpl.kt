package com.thejohnsondev.domain

import com.thejohnsondev.data.NotesRepository
import com.thejohnsondev.model.UserNotesResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNotesUseCaseImpl @Inject constructor(
    private val notesRepository: NotesRepository
) : GetAllNotesUseCase {
    override suspend fun invoke(userId: String): Flow<UserNotesResponse> {
        return notesRepository.getUserNotes(userId)
    }
}