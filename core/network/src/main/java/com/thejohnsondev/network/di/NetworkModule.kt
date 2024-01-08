package com.thejohnsondev.network.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.network.remote_datasource.firebase.FirebaseRemoteDataSourceImpl
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import com.thejohnsondev.network.remote_datasource.dotnet.ISafeDotNetApi
import com.thejohnsondev.network.remote_datasource.dotnet.ISafeDotNetRemoteDataSource
import com.thejohnsondev.network.remote_datasource.dotnet.ISafeDotNetRetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    @Singleton
    @Provides
    fun provideISafeDotNetApi(
        dataStore: DataStore
    ): ISafeDotNetApi = ISafeDotNetRetrofitService(
        dataStore
    ).invoke()

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @FirebaseRemoteDataSource
    @Singleton
    @Provides
    fun provideFirebaseRemoteDataSource(
        firebaseAuth: FirebaseAuth,
        firebaseDatabase: FirebaseDatabase,
        coroutineScope: CoroutineScope,
        dataStore: DataStore
    ): RemoteDataSource = FirebaseRemoteDataSourceImpl(
        firebaseAuth,
        firebaseDatabase,
        coroutineScope,
        dataStore
    )

    @DotNetRemoteDataSource
    @Singleton
    @Provides
    fun provideDotNetRemoteDataSource(
        iSafeDotNetApi: ISafeDotNetApi,
        coroutineScope: CoroutineScope,
        firebaseDatabase: FirebaseDatabase, //todo temporar
        firebaseAuth: FirebaseAuth, //todo temporar
        dataStore: DataStore
    ): RemoteDataSource = ISafeDotNetRemoteDataSource(
        iSafeDotNetApi,
        coroutineScope,
        firebaseDatabase, //todo temporar
        firebaseAuth, //todo temporar
        dataStore
    )

}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DotNetRemoteDataSource

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class FirebaseRemoteDataSource