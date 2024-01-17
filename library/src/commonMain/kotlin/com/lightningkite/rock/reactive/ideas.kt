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

class LateInitProperty<T>(): Writable<T>, ReadWriteProperty<Any?, T> {
    private val listeners = ArrayList<() -> Unit>()
    private var queued = ArrayList<Continuation<T>>()
    private var _value: T? = null
    var value: T
        get() = if(ready) _value as T else throw IllegalStateException()
        set(value) {
            _value = value
            ready = true
            val old = queued
            queued = ArrayList()
            old.forEach { it.resume(value) }
            listeners.toList().forEach { it() }
        }
    var ready: Boolean = false
        private set

    fun unset() {
        ready = false
        listeners.toList().forEach { it() }
    }

    override suspend infix fun set(value: T) {
        this.value = value
    }

    override suspend fun awaitRaw(): T {
        if (ready) return _value as T
        else return suspendCoroutineCancellable<T> { queued.add(it); return@suspendCoroutineCancellable { queued.remove(it) } }
    }

    override fun addListener(listener: () -> Unit): () -> Unit {
        listeners.add(listener)
        return {
            val pos = listeners.indexOfFirst { it === listener }
            if(pos != -1) {
                listeners.removeAt(pos)
            }
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = value
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) { this.value = value }
}

class Property<T>(startValue: T): Writable<T>, ReadWriteProperty<Any?, T> {
    private val listeners = ArrayList<() -> Unit>()
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
            val pos = listeners.indexOfFirst { it === listener }
            if(pos != -1) {
                listeners.removeAt(pos)
            }
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

private class ReactiveScopeData(var rerun: () -> Unit) : CoroutineContext.Element {
    val removers: HashMap<ResourceUse, () -> Unit> = HashMap()
    val latestPass: ArrayList<ResourceUse> = ArrayList()
    override val key: CoroutineContext.Key<ReactiveScopeData> = Key

    fun shutdown() {
        removers.forEach { it.value() }
        removers.clear()
        latestPass.clear()
        rerun = {}
    }
    object Key : CoroutineContext.Key<ReactiveScopeData>
}

fun CalculationContext.use(resourceUse: ResourceUse) {
    val x = resourceUse.start()
    onRemove { x() }
}

class Ref<T>(var value: T)

fun CalculationContext.reactiveScope(action: suspend () -> Unit) = reactiveScope(null, action)
fun CalculationContext.reactiveScope(onLoad: (()->Unit)?, action: suspend () -> Unit) {
    var run: () -> Unit = {}
    val data = ReactiveScopeData {
        run()
    }
    val name = Random.nextInt(1000000)
    var previousContext: CoroutineContext? = null
    var actionRef: Ref<suspend ()->Unit> = Ref(action)
    run = run@{
        val context: CoroutineContext = EmptyCoroutineContext.childCancellation() + data
        previousContext?.cancel()
        previousContext = context
        data.latestPass.clear()

        var done = false
        var loadStarted = false

        suspend { actionRef.value() }.startCoroutine(object : Continuation<Unit> {
            override val context: CoroutineContext = context

            // called when a coroutine ends. do nothing.
            override fun resumeWith(result: Result<Unit>) {
                done = true
                if(loadStarted) {
                    notifyComplete(result)
                }
                if(previousContext !== context) return
                for (entry in data.removers.entries.toList()) {
                    if (entry.key !in data.latestPass) {
                        entry.value()
                        data.removers.remove(entry.key)
                    }
                }
            }
        })

        if(!done) {
            // start load
            loadStarted = true
            notifyStart()
            onLoad?.invoke()
        }
    }
    run()
    this.onRemove {
        data.shutdown()
        run = {}
        actionRef.value = {}
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

/**
 * Desired behavior for shared:
 *
 * - Outside a reactive scope, [Readable.await] invokes the action with no sharing
 * - Inside a reactive scope, [Readable.await] starts the whole system listening and sharing the calculation.
 */
fun <T> shared(action: suspend CalculationContext.() -> T): Readable<T> {
    val removers = ArrayList<()->Unit>()
    val ctx = object: CalculationContext {
        override fun onRemove(action: () -> Unit) {
            removers.add(action)
        }
    }
    return object: Readable<T> {
        var value: T? = null
        var ready: Boolean = false
        var exception: Exception? = null
        var listening = false
        var queued = ArrayList<Continuation<T>>()
        override suspend fun awaitRaw(): T = if(ready) {
//            println("$this: Ready answer")
            exception?.let { throw it } ?: value as T
        } else if(listening) {
//            println("$this: Already listening; queue")
            suspendCoroutineCancellable<T> { queued.add(it); { queued.remove(it) } }
        } else {
//            println("$this: Nobody listening; action")
            action(ctx)
        }

        private val listeners = ArrayList<() -> Unit>()
        override fun addListener(listener: () -> Unit): () -> Unit {
            if (listeners.isEmpty()) {
                // startup
//                println("$this: Start listening")
                ctx.reactiveScope {
                    val listening = listening
//                    if (listening) println("$this: Recalculating...")
//                    else println("$this: Starting initial calculation...")
                    try {
                        val result = action(ctx)
                        value = result
                        exception = null
                    } catch (e: Exception) {
                        exception = e
                    } finally {
                        ready = true
                        if (listening) {
//                            println("$this: Change calculation complete, notifying")
                            // This is a change notification; notify our listeners
                            listeners.toList().forEach { it() }
                        } else {
//                            println("$this: Initial calculation complete")
                            // This is a first result; send to our queue
                            val e = exception
                            val result = value as T
                            val copy = queued
                            queued = ArrayList()
                            copy.forEach {
                                e?.let { e -> it.resumeWithException(e) } ?: it.resume(result)
                            }
                        }
                    }
                }
                listening = true
            }
            listeners.add(listener)
            return {
                removeListener(listener)
            }
        }
        private fun removeListener(listener: () -> Unit) {
            val pos = listeners.indexOfFirst { it === listener }
            if(pos != -1) {
                listeners.removeAt(pos)
            }
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