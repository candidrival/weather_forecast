package com.app.live.weather.forecast.domain.prefs

import kotlinx.coroutines.flow.Flow
import java.lang.reflect.Type

interface SecuritySharedPreferences {

    fun beginTransaction(block : SecuritySharedPreferences.()->Unit)

    fun putInt(name: String, value: Int)

    fun getInt(name: String, defaultValue: Int? = null): Int?

    fun putLong(name: String, value: Long)

    fun getLong(name: String, defaultValue: Long? = null): Long?

    fun putString(name: String, value: String)

    fun putStringNull(name: String, value: String?)

    fun getString(name: String, defaultValue: String? = null): String?

    fun putBoolean(name: String, value: Boolean)

    fun getBoolean(name: String, defaultValue: Boolean): Boolean

    fun getFlowBoolean(name: String, defaultValue: Boolean): Flow<Boolean>

    fun <T> putObjectAsJsonString(name: String, value: T) where T : Any?

    fun <T> getObjectFromJsonString(name: String, defaultValue: T? = null, type: Type): T? where T : Any

    fun createTimestamp(name: String)

    fun getTimestamp(name: String, defaultValue: Long? = null): Long

    fun remove(name: String)

    fun has(name: String): Boolean

    fun clear()

    fun <T> putListAsJsonString(key: String, list: List<T>?)

    fun <T> getListFromJsonString(name: String, elementType: Class<T>): List<T>
}