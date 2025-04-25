package com.app.live.weather.forecast.domain.api.utils

import androidx.annotation.Keep
import java.io.IOException

typealias GenericResponse<S> = NetworkResponse<S, String>
@Keep sealed class NetworkResponse<out T : Any, out U : Any> {
    /**
     * Success response with body
     */
    @Keep data class Success<T : Any>(val body: T) : NetworkResponse<T, Nothing>()

    sealed class NetworkError <U: Any> (): NetworkResponse<Nothing, U>(){
        /**
         * Called for 403 responses.
         * */
        @Keep data class ForbiddenNetworkError<U : Any>(val body: U? = null) : NetworkError<U>()

        /**
         * Called for 404 responses.
         * */
        @Keep data class NoUserFoundNetworkError<U : Any>(val body: U? = null) : NetworkError<U>()

        /**
         * Called for [400, 500) responses, except 403, 404.
         * */
        @Keep data class ClientNetworkError<U : Any>(val body: U? = null) : NetworkError<U>()

        /**
         * Called for [500, 600) response.
         */
        @Keep data class ServerNetworkError<U : Any>(val body: U? = null) : NetworkError<U>()

        /**
         * Called for network errors while making the call.
         */
        @Keep data class NetworkNetworkError(val error: IOException) : NetworkError<Nothing>()

        /**
         * Called for unexpected errors while making the call.
         * For example, json parsing error
         */
        @Keep data class UnknownNetworkError(val error: Throwable?) : NetworkError<Nothing>()
    }


}