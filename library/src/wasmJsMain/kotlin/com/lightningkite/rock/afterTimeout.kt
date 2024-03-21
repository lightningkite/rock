package com.lightningkite.rock

import kotlinx.browser.window
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.js.Promise

internal actual inline fun afterTimeout(milliseconds: Long, crossinline action: () -> Unit): () -> Unit {
    val handle = window.setTimeout({ ->
        action()
        null
    }, milliseconds.toInt())
    return {
        window.clearTimeout(handle)
    }
}

suspend fun <T: JsAny> Promise<T>.await(): T = suspendCoroutineCancellable { cont ->
    then(
        onFulfilled = {
            cont.resume(it)
            null
        },
        onRejected = {
            cont.resumeWithException(Exception(it.toString()))
            null
        }
    )
    return@suspendCoroutineCancellable {}
}