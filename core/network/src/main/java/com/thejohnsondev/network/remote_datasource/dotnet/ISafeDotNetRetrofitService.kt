package com.thejohnsondev.network.remote_datasource.dotnet

import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.network.common.EitherCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ISafeDotNetRetrofitService @Inject constructor(
    private val dataStore: DataStore
) {

    operator fun invoke(): ISafeDotNetApi {
        val okHttpBuilder = OkHttpClient.Builder()
        val okHttpClient = okHttpBuilder.build()
        val baseUrl =
            dataStore.getBaseUrl()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(EitherCallAdapterFactory())
            .build()
            .create(ISafeDotNetApi::class.java)
    }

}