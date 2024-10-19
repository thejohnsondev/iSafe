package com.thejohnsondev.data.di

import com.thejohnsondev.data.PasswordsRepository
import com.thejohnsondev.data.PasswordsRepositoryImpl
import com.thejohnsondev.database.local_datasource.LocalDataSource
import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun providePasswordsRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
        dataStore: DataStore
    ): PasswordsRepository = PasswordsRepositoryImpl(
        remoteDataSource,
        localDataSource,
        dataStore
    )

}