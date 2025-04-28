package com.app.live.weather.forecast.data.api.utils

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

internal class NetworkResponseCall<S : Any>(
    private val delegate: Call<S>
) : Call<NetworkResponse<S, String>> { // Replace E with String for simpler error handling

    override fun enqueue(callback: Callback<NetworkResponse<S, String>>) {
        delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()

                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(NetworkResponse.Success(body))
                    } else {
                        // Response is successful but body is null
                        callback.onResponse(NetworkResponse.NetworkError.ForbiddenNetworkError(null))
                    }
                } else {
                    val errorBody = error?.string() ?: "Unknown error" // Read the raw error body
                    Timber.e("Error body: $errorBody")

                    when (code) {
                        403 -> {
                            callback.onResponse(NetworkResponse.NetworkError.ForbiddenNetworkError(errorBody))
                        }
                        404 -> {
                            callback.onResponse(NetworkResponse.NetworkError.NoUserFoundNetworkError(errorBody))
                        }
                        in 400..499 -> {
                            callback.onResponse(NetworkResponse.NetworkError.ClientNetworkError(errorBody))
                        }
                        in 500..600 -> {
                            callback.onResponse(NetworkResponse.NetworkError.ServerNetworkError(errorBody))
                        }
                        else -> {
                            callback.onResponse(NetworkResponse.NetworkError.UnknownNetworkError(null))
                        }
                    }
                }
            }

            override fun onFailure(call: Call<S>, throwable: Throwable) {
                val networkResponse = when (throwable) {
                    is IOException -> NetworkResponse.NetworkError.NetworkNetworkError(throwable)
                    else -> NetworkResponse.NetworkError.UnknownNetworkError(throwable)
                }
                callback.onResponse(networkResponse)
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = NetworkResponseCall(delegate.clone())

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<NetworkResponse<S, String>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()

    private fun Callback<NetworkResponse<S, String>>.onResponse(response: NetworkResponse<S, String>) {
        this.onResponse(this@NetworkResponseCall, Response.success(response))
    }
}
