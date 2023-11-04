package com.thejohnsondev.domain.di

import com.thejohnsondev.data.NotesRepository
import com.thejohnsondev.domain.CreateNoteUseCase
import com.thejohnsondev.domain.CreateNoteUseCaseImpl
import com.thejohnsondev.domain.DeleteNoteUseCase
import com.thejohnsondev.domain.DeleteNoteUseCaseImpl
import com.thejohnsondev.domain.GetAllNotesUseCase
import com.thejohnsondev.domain.GetAllNotesUseCaseImpl
import com.thejohnsondev.domain.UpdateNoteUseCase
import com.thejohnsondev.domain.UpdateNoteUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotesDomainModule {

    @Singleton
    @Provides
    fun provideCreateNoteUseCase(notesRepository: NotesRepository): CreateNoteUseCase =
        CreateNoteUseCaseImpl(notesRepository)

    @Singleton
    @Provides
    fun provideDeleteNoteUseCase(notesRepository: NotesRepository): DeleteNoteUseCase =
        DeleteNoteUseCaseImpl(notesRepository)

    @Singleton
    @Provides
    fun provideGetAllNotesUseCase(notesRepository: NotesRepository): GetAllNotesUseCase =
        GetAllNotesUseCaseImpl(notesRepository)

    @Singleton
    @Provides
    fun provideUpdateNoteUseCase(notesRepository: NotesRepository): UpdateNoteUseCase =
        UpdateNoteUseCaseImpl(notesRepository)


}