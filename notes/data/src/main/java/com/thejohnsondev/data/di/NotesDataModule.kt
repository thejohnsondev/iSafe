package com.thejohnsondev.data.di

import com.google.firebase.database.FirebaseDatabase
import com.thejohnsondev.data.NotesRepository
import com.thejohnsondev.data.NotesRepositoryImpl
import com.thejohnsondev.datastore.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotesDataModule {

    @Singleton
    @Provides
    fun provideNotesRepository(
        firebaseDatabase: FirebaseDatabase,
        coroutineScope: CoroutineScope,
        dataStore: DataStore
    ): NotesRepository = NotesRepositoryImpl(
        firebaseDatabase,
        coroutineScope,
        dataStore
    )

}