package com.app.live.weather.forecast.utils.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

open class BaseViewModel : ViewModel(), KoinComponent {

    var statusBarHeight = MutableLiveData<Int>()

    var navigationBottomHeight = MutableLiveData<Int>()

    protected val _errorState = MutableStateFlow("")
    val errorState: StateFlow<String> = _errorState

    fun emitError(error: Throwable, message: String) = viewModelScope.launch {
        Timber.d("errorFlow:: $error with msg: $message")
        if(message.trim().isEmpty()) throw error
        _errorState.emit(message)
    }

    protected suspend inline fun <reified T: Any> T.safeCall(
        methodName: String,
        noinline onError: ((Exception) -> Unit)? = null,
        crossinline action: suspend () -> T
    ): T? {
        val callerName = T::class.java.simpleName // Get the caller class name dynamically
        Timber.tag(COROUTINE_TAG).i("safeCall $callerName : $methodName")
        return try {
            action()
        } catch (e: Exception) {
            if (e is CancellationException) throw e // Re-throw cancellation exceptions

            Timber.tag(COROUTINE_TAG).e("safeCall $callerName : $methodName with error: $e")
            emitError(e, e.stackTrace.toString())
            onError?.invoke(e)
            null
        }
    }

    /**
     * Executes a suspending action and safely handles errors without returning a value.
     */
    protected inline fun <reified T : Any> T.safeCall(
        methodName: String,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        noinline onError: ((Exception) -> Unit)? = null,
        crossinline action: suspend () -> Unit
    ): Job = viewModelScope.launch(dispatcher) {
        val callerName = T::class.java.simpleName // Get the caller class name dynamically
        Timber.tag(COROUTINE_TAG).i("safeCall $callerName : $methodName")
        try {
            action()
        } catch (e: Exception) {
            if (e is CancellationException) throw e // Re-throw cancellation exceptions

            Timber.tag(COROUTINE_TAG).e("safeCall $callerName : $methodName with error: $e")
            emitError(e, e.stackTrace.toString())
            onError?.invoke(e)
        }
    }

    companion object {
        const val COROUTINE_TAG = "Coroutine:: "
    }
}