package com.thejohnsondev.network.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.thejohnsondev.network.remote_datasource.FirebaseRemoteDataSourceImpl
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideRemoteDataSource(
        firebaseAuth: FirebaseAuth,
        firebaseDatabase: FirebaseDatabase
    ): RemoteDataSource = FirebaseRemoteDataSourceImpl(
        firebaseAuth,
        firebaseDatabase
    )
}