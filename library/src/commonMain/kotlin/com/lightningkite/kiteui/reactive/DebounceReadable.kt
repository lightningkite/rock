package com.lightningkite.kiteui.reactive

import com.lightningkite.kiteui.Cancellable
import com.lightningkite.kiteui.afterTimeout
import com.lightningkite.kiteui.delay
import com.lightningkite.kiteui.launchGlobal

data class DebounceReadable<T>(val source: Readable<T>, val milliseconds: Long) : Readable<T> {
    override var state: ReadableState<T> = ReadableState.notReady

    private var changeCount = 0
    private val listeners = ArrayList<() -> Unit>()
    override fun addListener(listener: () -> Unit): () -> Unit {
        var second: Cancellable? = null
        val first = source.addListener {
            val num = ++changeCount
            second = launchGlobal {
                delay(milliseconds)
                state = source.state
                if (num == changeCount) listener()
            }
        }
        return {
            first()
            second?.cancel()
        }
    }

    fun invokeAll() {
        listeners.toList().forEach { it() }
    }
}
data class DebounceListenable(val source: Listenable, val milliseconds: Long) : Listenable {
    private var changeCount = 0
    override fun addListener(listener: () -> Unit): () -> Unit {
        return source.addListener {
            val num = ++changeCount
            afterTimeout(milliseconds) {
                if (num == changeCount) listener()
            }
        }
    }
}

fun <T> Readable<T>.debounce(timeMs: Long): Readable<T> = DebounceReadable(this, timeMs)
fun Listenable.debounce(timeMs: Long): Listenable = DebounceListenable(this, timeMs)