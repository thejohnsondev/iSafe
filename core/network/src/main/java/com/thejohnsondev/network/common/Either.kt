package com.thejohnsondev.network.common

import arrow.core.Either
import com.thejohnsondev.model.ApiError
import com.thejohnsondev.model.HttpError
import com.thejohnsondev.model.NetworkError
import com.thejohnsondev.model.UnknownApiError
import okhttp3.Request
import okio.Timeout
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.reflect.Type
class EitherCall<R>(
    private val delegate: Call<R>,
    private val successType: Type
) : Call<Either<ApiError, R>> {

    override fun enqueue(callback: Callback<Either<ApiError, R>>) = delegate.enqueue(
        object : Callback<R> {

            override fun onResponse(call: Call<R>, response: Response<R>) {
                callback.onResponse(this@EitherCall, Response.success(response.toEither()))
            }

            private fun Response<R>.toEither(): Either<ApiError, R> {
                if (!isSuccessful) {
                    val errorBody = errorBody()?.string() ?: ""
                    return Either.Left(HttpError(code(), errorBody))
                }

                body()?.let { body -> return Either.Right(body) }

                return if (successType == Unit::class.java) {
                    @Suppress("UNCHECKED_CAST")
                    Either.Right(Unit) as Either<ApiError, R>
                } else {
                    @Suppress("UNCHECKED_CAST")
                    Either.Left(UnknownError("Response body was null")) as Either<ApiError, R>
                }
            }

            override fun onFailure(call: Call<R>, throwable: Throwable) {
                val error = when (throwable) {
                    is IOException -> NetworkError(throwable)
                    else -> UnknownApiError(throwable)
                }
                callback.onResponse(this@EitherCall, Response.success(Either.Left(error)))
            }
        }
    )

    override fun clone(): Call<Either<ApiError, R>> {
        TODO("Not yet implemented")
    }

    override fun execute(): Response<Either<ApiError, R>> {
        TODO("Not yet implemented")
    }

    override fun isExecuted(): Boolean {
        TODO("Not yet implemented")
    }

    override fun cancel() {
        TODO("Not yet implemented")
    }

    override fun isCanceled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun request(): Request {
        TODO("Not yet implemented")
    }

    override fun timeout(): Timeout {
        TODO("Not yet implemented")
    }

}