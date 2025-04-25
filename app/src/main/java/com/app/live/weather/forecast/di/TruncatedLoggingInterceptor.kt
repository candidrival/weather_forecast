package com.app.live.weather.forecast.di

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import timber.log.Timber

class TruncatedLoggingInterceptor(private val maxBodyLength: Int = 1000) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Log request details
        //logRequest(request)

        val response = chain.proceed(request)

        // Log response details with truncation for large bodies
        return logResponse(response)
    }


    private fun logResponse(response: Response): Response {
        val responseBody = response.body
        val responseLog = StringBuilder()
            .append("<-- ${response.code} ${response.message} ${response.request.url}\n")

        // Log headers
        for (header in response.headers) {
            responseLog.append("${header.first}: ${header.second}\n")
        }

        // Log body (if exists and is readable)
        responseBody?.let {
            val source = it.source()
            source.request(Long.MAX_VALUE) // Load entire body
            val buffer = source.buffer.clone() // Clone buffer to avoid consuming the original
            val bodyString = buffer.readUtf8()
            responseLog.append("Body: ${truncate(bodyString)}\n")
        }

        responseLog.append("<-- END HTTP")


        // Return a new response to avoid consuming the original body
        return response.newBuilder()
            .body((responseBody?.bytes() ?: ByteArray(0)).toResponseBody(responseBody?.contentType()))
            .build()
    }

    private fun truncate(value: String): String {
        return if (value.length > maxBodyLength) {
            value.take(maxBodyLength) + "... [truncated]"
        } else {
            value
        }
    }
}