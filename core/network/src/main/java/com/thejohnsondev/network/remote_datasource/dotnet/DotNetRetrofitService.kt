package com.thejohnsondev.network.remote_datasource.dotnet

import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.network.common.EitherCallAdapterFactory
import com.thejohnsondev.network.remote_datasource.dotnet.interceptors.AuthTokenInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class DotNetRetrofitService @Inject constructor(
    private val dataStore: DataStore,
    private val authTokenInterceptor: AuthTokenInterceptor
) {

    operator fun invoke(): DotNetApi {
        val baseUrl =
            dataStore.getBaseUrl()
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authTokenInterceptor)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(EitherCallAdapterFactory())
            .build()
            .create(DotNetApi::class.java)
    }

}