package com.thejohnsondev.network.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.network.remote_datasource.RemoteDataSource
import com.thejohnsondev.network.remote_datasource.dotnet.DotNetApi
import com.thejohnsondev.network.remote_datasource.dotnet.DotNetRemoteDataSource
import com.thejohnsondev.network.remote_datasource.dotnet.DotNetRetrofitService
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
    fun provideDotNetApi(
        dataStore: DataStore,
        authTokenInterceptor: AuthTokenInterceptor
    ): DotNetApi = DotNetRetrofitService(
        dataStore,
        authTokenInterceptor
    ).invoke()

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()


    @Singleton
    @Provides
    fun provideDotNetRemoteDataSource(
        dotNetApi: DotNetApi,
    ): RemoteDataSource = DotNetRemoteDataSource(
        dotNetApi
    )

}