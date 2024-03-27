package com.lightningkite.rock.reactive

import com.lightningkite.rock.*
import com.lightningkite.rock.afterTimeout
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.jvm.JvmName


infix fun <T> Writable<T>.equalTo(value: T): Writable<Boolean> = object : Writable<Boolean> {
    override val state: ReadableState<Boolean> get() = this@equalTo.state.map { it == value }
    override fun addListener(listener: () -> Unit): () -> Unit = this@equalTo.addListener(listener)
    val target = value
    override suspend fun set(value: Boolean) {
        if (value) this@equalTo.set(target)
    }
}

infix fun <T> Writable<T>.equalToDynamic(value: suspend () -> T): Writable<Boolean> = shared {
    await() == value()
}.withWrite {
    if (it) set(value())
}


infix fun <T> Writable<T>.bind(master: Writable<T>) {
    with(CalculationContextStack.current()) {
        launch {
            this@bind.set(master.await())
        }
        var setting = false
        master.addListener {
            if (setting) return@addListener
            setting = true
            launch {
                this@bind.set(master.await())
            }
            setting = false
        }.also { onRemove(it) }
        this@bind.addListener {
            if (setting) return@addListener
            setting = true
            launch {
                master.set(this@bind.await())
            }
            setting = false
        }.also { onRemove(it) }
    }
}

infix fun <T> Writable<Set<T>>.contains(value: T): Writable<Boolean> = shared { value in await() }.withWrite { on ->
    if (on) this@contains.set(this@contains.await() + value)
    else this@contains.set(this@contains.await() - value)
}

fun <T> Readable<T>.withWrite(action: suspend Readable<T>.(T) -> Unit): Writable<T> =
    object : Writable<T>, Readable<T> by this {
        override suspend fun set(value: T) {
            action(this@withWrite, value)
        }
    }

@JvmName("writableIntAsString")
fun Writable<Int>.asString(): Writable<String> = shared { this@asString.await().toString() }
    .withWrite { it.toIntOrNull()?.let { this@asString.set(it) } }

@JvmName("writableLongAsString")
fun Writable<Long>.asString(): Writable<String> = shared { this@asString.await().toString() }
    .withWrite { it.toLongOrNull()?.let { this@asString.set(it) } }

@JvmName("writableFloatAsString")
fun Writable<Float>.asString(): Writable<String> = shared { this@asString.await().toString() }
    .withWrite { it.toFloatOrNull()?.let { this@asString.set(it) } }

@JvmName("writableDoubleAsString")
fun Writable<Double>.asString(): Writable<String> = shared { this@asString.await().toString() }
    .withWrite { it.toDoubleOrNull()?.let { this@asString.set(it) } }

@JvmName("writableIntNullableAsString")
fun Writable<Int?>.asString(): Writable<String> = shared { this@asString.await()?.toString() ?: "" }
    .withWrite { this@asString.set(it.toIntOrNull()) }

@JvmName("writableLongNullableAsString")
fun Writable<Long?>.asString(): Writable<String> = shared { this@asString.await()?.toString() ?: "" }
    .withWrite { this@asString.set(it.toLongOrNull()) }

@JvmName("writableFloatNullableAsString")
fun Writable<Float?>.asString(): Writable<String> = shared { this@asString.await()?.toString() ?: "" }
    .withWrite { this@asString.set(it.toFloatOrNull()) }

@JvmName("writableDoubleNullableAsString")
fun Writable<Double?>.asString(): Writable<String> = shared { this@asString.await()?.toString() ?: "" }
    .withWrite { this@asString.set(it.toDoubleOrNull()) }

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

fun <T> Readable<T>.debounce(timeMs: Long): Readable<T> = DebounceReadable(this, timeMs)
fun Listenable.debounce(timeMs: Long): Listenable = DebounceListenable(this, timeMs)