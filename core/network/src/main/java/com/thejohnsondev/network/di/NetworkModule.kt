package com.thejohnsondev.network.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import com.thejohnsondev.network.remote_datasource.dotnet.ISafeDotNetApi
import com.thejohnsondev.network.remote_datasource.dotnet.ISafeDotNetRemoteDataSource
import com.thejohnsondev.network.remote_datasource.dotnet.ISafeDotNetRetrofitService
import com.thejohnsondev.network.remote_datasource.dotnet.interceptors.AuthTokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    fun provideAuthTokenInterceptor(dataStore: DataStore): AuthTokenInterceptor =
        AuthTokenInterceptor(dataStore)

    @Singleton
    @Provides
    fun provideISafeDotNetApi(
        dataStore: DataStore,
        authTokenInterceptor: AuthTokenInterceptor
    ): ISafeDotNetApi = ISafeDotNetRetrofitService(
        dataStore,
        authTokenInterceptor
    ).invoke()

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()


    @DotNetRemoteDataSource
    @Singleton
    @Provides
    fun provideDotNetRemoteDataSource(
        iSafeDotNetApi: ISafeDotNetApi,
    ): RemoteDataSource = ISafeDotNetRemoteDataSource(
        iSafeDotNetApi
    )

}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DotNetRemoteDataSource