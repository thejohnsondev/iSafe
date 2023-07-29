package com.thejohnsondev.isafe.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.thejohnsondev.isafe.domain.use_cases.auth.IsUserLoggedInUseCase
import com.thejohnsondev.isafe.domain.use_cases.auth.IsUserLoggedInUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    @Singleton
    @Provides
    fun provideIsUserLoggedInUseCase(firebaseAuth: FirebaseAuth): IsUserLoggedInUseCase =
        IsUserLoggedInUseCaseImpl(firebaseAuth)

}