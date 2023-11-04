package com.thejohnsondev.datastore.di

import android.content.Context
import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.datastore.DataStoreImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext applicationContext: Context): DataStore =
        DataStoreImpl(applicationContext)

}