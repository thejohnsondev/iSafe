package com.thejohnsondev.data.di

import com.thejohnsondev.common.key_utils.KeyUtils
import com.thejohnsondev.data.AuthRepository
import com.thejohnsondev.data.AuthRepositoryImpl
import com.thejohnsondev.data.GenerateKeyRepository
import com.thejohnsondev.data.GenerateKeyRepositoryImpl
import com.thejohnsondev.database.local_datasource.LocalDataSource
import com.thejohnsondev.datastore.DataStore
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
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
        dataStore: DataStore,
        keyUtils: KeyUtils
    ): AuthRepository =
        AuthRepositoryImpl(
            remoteDataSource,
            localDataSource,
            dataStore,
            keyUtils
        )
    // TODO: Replace provides with bind for all repos

    @Singleton
    @Provides
    fun provideGenerateKeyRepository(
        coroutineScope: CoroutineScope,
        keyUtils: KeyUtils
    ): GenerateKeyRepository = GenerateKeyRepositoryImpl(coroutineScope, keyUtils)


}