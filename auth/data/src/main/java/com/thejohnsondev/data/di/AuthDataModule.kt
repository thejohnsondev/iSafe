package com.thejohnsondev.data.di

import com.google.firebase.database.FirebaseDatabase
import com.thejohnsondev.data.AuthRepository
import com.thejohnsondev.data.AuthRepositoryImpl
import com.thejohnsondev.data.UserRepository
import com.thejohnsondev.data.UserRepositoryImpl
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
    fun provideAuthRepository(coroutineScope: CoroutineScope): AuthRepository =
        AuthRepositoryImpl(coroutineScope)

    @Singleton
    @Provides
    fun provideUserRepository(
        firebaseDatabase: FirebaseDatabase,
        coroutineScope: CoroutineScope,
    ): UserRepository =
        UserRepositoryImpl(firebaseDatabase, coroutineScope)

}