package com.thejohnsondev.data.di

import com.thejohnsondev.data.ToolsRepository
import com.thejohnsondev.data.ToolsRepositoryImpl
import com.thejohnsondev.network.di.DotNetRemoteDataSource
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ToolsDataModule {

    @Provides
    @Singleton
    fun provideToolsRepository(
        @DotNetRemoteDataSource remoteDataSource: RemoteDataSource
    ): ToolsRepository = ToolsRepositoryImpl(
        remoteDataSource
    )

}