package com.thejohnsondev.data.di

import com.google.firebase.database.FirebaseDatabase
import com.thejohnsondev.data.PasswordsRepository
import com.thejohnsondev.data.PasswordsRepositoryImpl
import com.thejohnsondev.datastore.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    // TODO: remove from the app module and uncomment

//    @Singleton
//    @Provides
//    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()
//
//    @Singleton
//    @Provides
//    fun provideIOCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)

    @Singleton
    @Provides
    fun providePasswordsRepository(
        firebaseDatabase: FirebaseDatabase,
        coroutineScope: CoroutineScope,
        dataStore: DataStore
    ): PasswordsRepository = PasswordsRepositoryImpl(
        firebaseDatabase,
        coroutineScope,
        dataStore
    )

}