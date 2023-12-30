package com.thejohnsondev.data.di

import com.thejohnsondev.data.AuthRepository
import com.thejohnsondev.data.AuthRepositoryImpl
import com.thejohnsondev.data.UserRepository
import com.thejohnsondev.data.UserRepositoryImpl
import com.thejohnsondev.network.di.FirebaseRemoteDataSource
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthDataModule {

    @Singleton
    @Provides
    fun provideAuthRepository(@FirebaseRemoteDataSource remoteDataSource: RemoteDataSource): AuthRepository =
        AuthRepositoryImpl(remoteDataSource)

    @Singleton
    @Provides
    fun provideUserRepository(
        @FirebaseRemoteDataSource remoteDataSource: RemoteDataSource
    ): UserRepository = UserRepositoryImpl(remoteDataSource)

}