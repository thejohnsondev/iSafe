package com.thejohnsondev.domain.di

import com.thejohnsondev.data.GenerateKeyRepository
import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.domain.GenerateUserKeyUseCase
import com.thejohnsondev.domain.GenerateUserKeyUseCaseImpl
import com.thejohnsondev.domain.SaveUserKeyUseCase
import com.thejohnsondev.domain.SaveUserKeyUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GenerateKeyDomainModule {

    @Singleton
    @Provides
    fun provideSaveUserKeyUseCase(
        dataStore: DataStore
    ): SaveUserKeyUseCase = SaveUserKeyUseCaseImpl(dataStore)

    @Singleton
    @Provides
    fun provideGenerateUserKeyUseCase(
        generateKeyRepository: GenerateKeyRepository
    ): GenerateUserKeyUseCase = GenerateUserKeyUseCaseImpl(generateKeyRepository)
}