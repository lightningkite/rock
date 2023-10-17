package com.lightningkite.rock

import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.ViewContext
import com.lightningkite.rock.views.onRemove
import kotlin.coroutines.*

class CancelledException(): Exception()
suspend fun <T> suspendCoroutineCancellable(start: (Continuation<T>)->()->Unit): T {
    stopIfCancelled()
    var canceller: (()->Unit)? = null
    val result: T = try {
        suspendCoroutine<T> {
            canceller = start(SingleThreadContinuation(it))
        }
    } catch(e: CancelledException) {
        canceller?.invoke()
        throw e
    }
    return result
}

suspend fun delay(milliseconds: Long) = suspendCoroutineCancellable<Unit> {
    val handle = afterTimeout(milliseconds) { ->
        it.resume(Unit)
    }
    return@suspendCoroutineCancellable {
        handle()
    }
}

private class SingleThreadContinuation<T>(val wraps: Continuation<T>): Continuation<T> {
    var done = false
    override fun resumeWith(result: Result<T>) {
        if(done) return
        done = true
        wraps.resumeWith(result)
    }

    override val context: CoroutineContext
        get() = wraps.context
}

suspend fun <T> race(vararg actions: suspend () -> T): T = suspendCoroutineCancellable { top ->
    val ctx = top.context + CancellationState(false, top.context[CancellationState.Key])
    actions.forEach {
        it.startCoroutine(object: Continuation<T> {
            override val context: CoroutineContext = ctx
            override fun resumeWith(result: Result<T>) {
                top.resumeWith(result)
                ctx.cancel()
            }
        })
    }
    return@suspendCoroutineCancellable {
        ctx.cancel()
    }
}

class TimeoutException(): Exception()
suspend fun <T> timeout(milliseconds: Long, action: suspend () -> T): T {
    return race(
        action,
        {
            delay(milliseconds)
            throw TimeoutException()
        }
    )
}
suspend fun <T> timeoutOrNull(milliseconds: Long, action: suspend () -> T): T? {
    return race(
        action,
        {
            delay(milliseconds)
            null
        }
    )
}

private fun CoroutineContext.childCancellation(): CoroutineContext = this + CancellationState(false, this.get(CancellationState.Key))
private class CancellationState(var stop: Boolean, var parent: CancellationState? = null): CoroutineContext.Element {
    override val key: CoroutineContext.Key<CancellationState> = Key
    val shouldStop: Boolean get() = stop || (parent?.stop ?: false)
    object Key: CoroutineContext.Key<CancellationState>
}
private fun CoroutineContext.cancel() {
    this[CancellationState.Key]!!.stop = true
}
suspend fun stopIfCancelled() {
    val state = coroutineContext[CancellationState.Key]!!
    if(state.shouldStop) {
        throw CancelledException()
    }
}

interface Async<T>: Cancellable {
    suspend fun await(): T
}

suspend fun <T> async(action: suspend ()->T): Async<T> {
    val context: CoroutineContext = coroutineContext.childCancellation()
    var toReturn: Result<T>? = null
    var otherResume: ((Result<T>) -> Unit)? = null
    action.startCoroutine(object : Continuation<T> {
        override val context: CoroutineContext = context
        // called when a coroutine ends. do nothing.
        override fun resumeWith(result: Result<T>) {
            val otherResume = otherResume
            if(otherResume != null) otherResume(result)
            else toReturn = result
        }
    })
    return object: Async<T> {
        override suspend fun await(): T = suspendCoroutineCancellable<T> {
            val toReturn = toReturn
            if(toReturn != null) {
                it.resumeWith(toReturn)
                return@suspendCoroutineCancellable {}
            } else {
                otherResume = it::resumeWith
                return@suspendCoroutineCancellable {
                    context.cancel()
                }
            }
        }
        override fun cancel() {
            context.cancel()
        }
    }
}

interface Cancellable {
    fun cancel()
}

fun launchGlobal(action: suspend () -> Unit): Cancellable {
    val context: CoroutineContext = EmptyCoroutineContext.childCancellation()
    action.startCoroutine(object : Continuation<Unit> {
        override val context: CoroutineContext = context
        // called when a coroutine ends. do nothing.
        override fun resumeWith(result: Result<Unit>) {
            result.onFailure { ex : Throwable -> println(ex.message) }
        }
    })
    return object: Cancellable {
        override fun cancel() {
            context.cancel()
        }
    }
}

fun ViewContext.launch(action: suspend () -> Unit) {
    val cancel = launchGlobal(action)
    this.onRemove.onRemove { cancel.cancel() }
}
fun NView.launch(action: suspend () -> Unit) {
    val cancel = launchGlobal(action)
    this.onRemove.onRemove { cancel.cancel() }
}