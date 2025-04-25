package com.app.live.weather.forecast.domain.api.utils

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import java.lang.reflect.Type

class NetworkResponseAdapter<S : Any>(
    private val successType: Type,
) : CallAdapter<S, Call<NetworkResponse<S, String>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<S>): Call<NetworkResponse<S, String>> {
        return NetworkResponseCall(call)
    }
}