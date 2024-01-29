package com.thejohnsondev.common.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Singleton
    @Provides
    fun provideApplicationContext(application: Application): Context =
        application.applicationContext

    @Singleton
    @Provides
    fun provideIOCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)
}