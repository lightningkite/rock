package com.lightningkite.rock

import kotlinx.browser.window
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.js.Promise

internal actual inline fun afterTimeout(milliseconds: Long, crossinline action: () -> Unit): () -> Unit {
    val handle = window.setTimeout({ ->
        action()
    }, milliseconds.toInt())
    return {
        window.clearTimeout(handle)
    }
}

suspend fun <T> Promise<T>.await(): T = suspendCancellableCoroutine { cont ->
    println("Promise: Inside suspendCoroutineCancellable")
    then(
        onFulfilled = {
            println("Promise: OnFulfilled")
            cont.resume(it)
        },
        onRejected = {
            println("Promise: OnRejected")
            cont.resumeWithException(it)
        }
    )
    cont.invokeOnCancellation {
        println("Promise: Cancelled")
    }
}