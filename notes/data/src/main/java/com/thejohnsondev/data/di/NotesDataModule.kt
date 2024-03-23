package com.thejohnsondev.data.di

import com.thejohnsondev.data.NotesRepository
import com.thejohnsondev.data.NotesRepositoryImpl
import com.thejohnsondev.network.di.DotNetRemoteDataSource
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotesDataModule {

    @Singleton
    @Provides
    fun provideNotesRepository(
        @DotNetRemoteDataSource remoteDataSource: RemoteDataSource
    ): NotesRepository = NotesRepositoryImpl(
        remoteDataSource
    )

}