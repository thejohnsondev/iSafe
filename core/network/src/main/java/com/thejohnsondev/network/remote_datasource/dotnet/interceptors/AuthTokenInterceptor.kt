package com.thejohnsondev.network.remote_datasource.dotnet.interceptors

import com.thejohnsondev.datastore.DataStore
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthTokenInterceptor @Inject constructor(
    private val dataStore: DataStore
) : Interceptor {

    companion object {
        private val TOKEN_PREFIX = "Bearer "
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = dataStore.getUserToken()
        if (token.isEmpty()) return chain.proceed(chain.request())
        val fullToken = "$TOKEN_PREFIX$token"
        val request = chain.request()
        val requestBuilder = request.newBuilder()
            .addHeader("Authorization", fullToken)

        return chain.proceed(requestBuilder.build())

    }
}