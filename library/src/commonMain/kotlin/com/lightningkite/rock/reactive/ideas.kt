package com.lightningkite.rock.reactive

import com.lightningkite.rock.*
import kotlin.coroutines.*
import kotlin.properties.ReadWriteProperty
import kotlin.random.Random
import kotlin.reflect.KProperty

interface ResourceUse {
    fun start(): () -> Unit
}

interface Listenable : ResourceUse {
    /**
     * Adds the [listener] to be called every time this event fires.
     * @return a function to remove the [listener] that was added.  Removing multiple times should not cause issues.
     */
    fun addListener(listener: () -> Unit): () -> Unit
    override fun start(): () -> Unit = addListener { }
}

interface Readable<out T> : Listenable {
    suspend fun awaitRaw(): T
}

interface Writable<T> : Readable<T> {
    suspend infix fun set(value: T)
}

suspend infix fun <T> Writable<T>.modify(action: suspend (T) -> T) {
    set(action(await()))
}

class Property<T>(startValue: T) : Writable<T>, ReadWriteProperty<Any?, T> {
    private val listeners = HashSet<() -> Unit>()
    var value: T = startValue
        set(value) {
            field = value
            listeners.toList().forEach { it() }
        }

    override suspend infix fun set(value: T) {
        this.value = value
    }

    override suspend fun awaitRaw(): T = value

    override fun addListener(listener: () -> Unit): () -> Unit {
        listeners.add(listener)
        return {
            listeners.remove(listener)
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = value
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) { this.value = value }
}

class Constant<T>(val value: T) : Readable<T> {
    companion object {
        private val NOOP = {}
    }
    override fun addListener(listener: () -> Unit): () -> Unit = NOOP
    override suspend fun awaitRaw(): T = value
}

private class ReactiveScopeData(val rerun: () -> Unit) : CoroutineContext.Element {
    val removers: HashMap<ResourceUse, () -> Unit> = HashMap()
    val latestPass: HashSet<ResourceUse> = HashSet()
    override val key: CoroutineContext.Key<ReactiveScopeData> = Key

    object Key : CoroutineContext.Key<ReactiveScopeData>
}

fun CalculationContext.use(resourceUse: ResourceUse) {
    val x = resourceUse.start()
    onRemove { x() }
}

fun CalculationContext.reactiveScope(action: suspend () -> Unit) {
//    var queueRerun = false
//    var isRunning = false
    var run: () -> Unit = {}
    val data = ReactiveScopeData {
        run()
    }
    var previousContext: CoroutineContext? = null
    run = run@{
        notifyStart()
        val context: CoroutineContext = EmptyCoroutineContext.childCancellation() + data
        previousContext?.cancel()
        previousContext = context
        data.latestPass.clear()
//        if (isRunning) {
//            queueRerun = true
//            return@run
//        }
//        isRunning = true
        action.startCoroutine(object : Continuation<Unit> {
            override val context: CoroutineContext = context

            // called when a coroutine ends. do nothing.
            override fun resumeWith(result: Result<Unit>) {
                if(previousContext !== context) return
                for (entry in data.removers.entries.toList()) {
                    if (entry.key !in data.latestPass) {
                        entry.value()
                        data.removers.remove(entry.key)
                    }
                }
//                isRunning = false
//
//                if (queueRerun) {
//                    queueRerun = false
//                    run()
//                }
                if(result.isSuccess) notifySuccess()
                else result.exceptionOrNull()?.let {
                    if(it !is CancelledException) {
                        notifyFailure(it)
                    }
                }
                result.onFailure { ex: Throwable ->
                    if(ex is CancelledException) return@onFailure
                    ex.printStackTrace()
                }
            }
        })
    }
    run()
    this.onRemove {
        data.removers.forEach { it.value() }
        data.removers.clear()
        previousContext?.cancel()
    }
}

suspend fun rerunOn(listenable: Listenable) {
    coroutineContext[ReactiveScopeData.Key]?.let { data ->
        if (!data.removers.containsKey(listenable)) {
            data.removers[listenable] = listenable.addListener { data.rerun() }
        }
        data.latestPass.add(listenable)
    }
}

suspend fun <T> Readable<T>.await(): T {
    rerunOn(this)
    return awaitRaw()
}

fun <T> shared(action: suspend CalculationContext.() -> T): Readable<T> {
    val removers = ArrayList<()->Unit>()
    val ctx = object: CalculationContext {
        override fun notifyStart() {}
        override fun notifySuccess() {}
        override fun notifyFailure(t: Throwable) { t.printStackTrace() }
        override fun onRemove(action: () -> Unit) {
            removers.forEach { it() }
            removers.clear()
        }
    }
    val id = Random.nextInt(100, 999)
    return object: Readable<T> {
        var value: T? = null
        var ready: Boolean = false
        var exception: Exception? = null
        var listening = false
        val queued = ArrayList<Continuation<T>>()
        override suspend fun awaitRaw(): T = if(ready) {
            exception?.let { throw it } ?: value as T
        } else if(listening) {
            suspendCoroutineCancellable<T> { queued.add(it); { queued.remove(it) } }
        } else action(ctx)
        private val listeners = HashSet<() -> Unit>()
        override fun addListener(listener: () -> Unit): () -> Unit {
            if (listeners.isEmpty()) {
                // startup
                listening = true
                ctx.reactiveScope {
                    try {
                        value = action(ctx)
                        exception = null
                    } catch (e: Exception) {
                        exception = e
                    } finally {
                        ready = true
                    }
                    listeners.forEach { it() }
                }
            }
            listeners.add(listener)
            return {
                removeListener(listener)
            }
        }
        private fun removeListener(listener: () -> Unit) {
            listeners.remove(listener)
            if (listeners.isEmpty()) {
                // shutdown
                removers.forEach { it() }
                ready = false
                listening = false
                value = null
                exception = null
            }
        }
    }
}

private class WaitForNotNull<T: Any>(val wraps: Readable<T?>): Readable<T> {
    override suspend fun awaitRaw(): T {
        val basis = wraps.awaitRaw()
        if(basis == null) return suspendCoroutineCancellable<T> { {  } }
        else return basis
    }

    override fun addListener(listener: () -> Unit): () -> Unit {
        return wraps.addListener(listener)
    }

    override fun hashCode(): Int = wraps.hashCode() + 1

    override fun equals(other: Any?): Boolean = other is WaitForNotNull<*> && this.wraps == other.wraps
}

val <T: Any> Readable<T?>.waitForNotNull: Readable<T> get() = WaitForNotNull(this)
suspend fun <T: Any> Readable<T?>.awaitNotNull(): T {
    val basis = await()
    if(basis == null) return suspendCoroutineCancellable<T> { {} }
    else return basis
}