package com.thejohnsondev.data.di

import com.thejohnsondev.data.AuthRepository
import com.thejohnsondev.data.AuthRepositoryImpl
import com.thejohnsondev.data.GenerateKeyRepository
import com.thejohnsondev.data.GenerateKeyRepositoryImpl
import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.network.di.DotNetRemoteDataSource
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthDataModule {

    @Singleton
    @Provides
    fun provideAuthRepository(
        @DotNetRemoteDataSource remoteDataSource: RemoteDataSource,
        dataStore: DataStore
    ): AuthRepository =
        AuthRepositoryImpl(
            remoteDataSource,
            dataStore
        )

    @Singleton
    @Provides
    fun provideGenerateKeyRepository(
        coroutineScope: CoroutineScope
    ): GenerateKeyRepository = GenerateKeyRepositoryImpl(coroutineScope)


}