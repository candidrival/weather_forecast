package com.app.live.weather.forecast.data.prefs

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import java.lang.reflect.Type
import java.util.Calendar

class SecuritySharedPreferencesImpl(
    context: Context,
    prefName: String
) : SecuritySharedPreferences {


    private var sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        try {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            sharedPreferences = EncryptedSharedPreferences.create(
                prefName,
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            // Handle corrupted keyset or other initialization issues
            Log.e("SecuritySharedPrefs", "Initialization failed: ${e.message}")
            // Fallback to regular SharedPreferences or clear corrupted data
            sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        }
        editor = sharedPreferences.edit()
    }

    override fun putInt(name: String, value: Int) {
        editor.putInt(name, value).apply()
    }

    override fun getInt(name: String, defaultValue: Int?): Int = sharedPreferences.getInt(name, defaultValue ?: 0)
    override fun putLong(name: String, value: Long) {
        editor.putLong(name,value)
    }

    override fun getLong(name: String, defaultValue: Long?): Long =
        sharedPreferences.getLong(name,defaultValue ?: 0)

    override fun putString(name: String, value: String) {
        editor.putString(name, value).apply()
    }

    override fun putStringNull(name: String, value: String?) {
        editor.putString(name, value).apply()
    }

    override fun getString(name: String, defaultValue: String?): String? =
        sharedPreferences.getString(name, defaultValue)

    override fun putBoolean(name: String, value: Boolean) {
        editor.putBoolean(name, value).apply()
    }

    override fun getBoolean(name: String, defaultValue: Boolean): Boolean = sharedPreferences.getBoolean(name, defaultValue)

    override fun getFlowBoolean(name: String, defaultValue: Boolean): Flow<Boolean> {
        return sharedPreferences.getBooleanFlowForKey(name)
    }

    override fun <T : Any?> putObjectAsJsonString(name: String, value: T) {
        val jsonString = Gson().toJson(value)
        putString(name, jsonString)
    }

    override fun <T : Any> getObjectFromJsonString(name: String, defaultValue: T?, type: Type): T? {
        if (has(name)) {
            return getString(name)?.let { Gson().fromJson(it, type)}
        }

        return defaultValue
    }

    override fun <T> putListAsJsonString(key: String, list: List<T>?) {
        val gson = Gson()
        val json = gson.toJson(list)
        putString(key, json)
    }

    override fun <T : Any?> getListFromJsonString(name: String, elementType: Class<T>): List<T> {
        val jsonString = getString(name, "")
        return if (jsonString.isNullOrEmpty()) {
            emptyList()
        } else {
            val gson = Gson()
            val type: Type = TypeToken.getParameterized(List::class.java, elementType).type
            gson.fromJson(jsonString, type)
        }
    }

    override fun createTimestamp(name: String) {
        editor.putLong(name, Calendar.getInstance().timeInMillis).apply()
    }

    override fun getTimestamp(name: String, defaultValue: Long?): Long =
        sharedPreferences.getLong(name, defaultValue ?: Calendar.getInstance().timeInMillis)

    override fun remove(name: String) {
        editor.remove(name).apply()
    }

    override fun has(name: String): Boolean = sharedPreferences.contains(name)

    override fun clear() = editor.clear().apply()

    override fun beginTransaction(block: SecuritySharedPreferences.() -> Unit) = block(this)


    fun SharedPreferences.getBooleanFlowForKey(
        keyForString: String
    ): Flow<Boolean> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (keyForString == key) {
                trySend(this@getBooleanFlowForKey.getBooleanValue(key))
            }
        }
        registerOnSharedPreferenceChangeListener(listener)
        if (contains(keyForString)) {
            send(this@getBooleanFlowForKey.getBooleanValue(keyForString))
        }
        awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
    }.buffer(Channel.UNLIMITED)

    private fun SharedPreferences?.getBooleanValue(
        key: String
    ): Boolean = this?.getBoolean(key, false) ?: false
}