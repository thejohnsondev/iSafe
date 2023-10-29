package com.thejohnsondev.data.di

import android.content.Context
import com.thejohnsondev.data.GenerateKeyRepository
import com.thejohnsondev.data.GenerateKeyRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GenerateKeyDataModule {

    @Singleton
    @Provides
    fun provideGenerateKeyRepository(
        coroutineScope: CoroutineScope,
        applicationContext: Context
    ): GenerateKeyRepository = GenerateKeyRepositoryImpl(coroutineScope, applicationContext)

}