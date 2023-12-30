package com.thejohnsondev.data.di

import com.thejohnsondev.data.PasswordsRepository
import com.thejohnsondev.data.PasswordsRepositoryImpl
import com.thejohnsondev.network.di.FirebaseRemoteDataSource
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
        @FirebaseRemoteDataSource remoteDataSource: RemoteDataSource
    ): PasswordsRepository = PasswordsRepositoryImpl(
        remoteDataSource
    )

}