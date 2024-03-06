package com.lightningkite.rock.reactive

import com.lightningkite.rock.*
import kotlin.coroutines.*
import kotlin.properties.ReadWriteProperty
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

interface ImmediateReadable<out T> : Readable<T> {
    val value: T
}

interface ImmediateWritable<T> : Writable<T>, ImmediateReadable<T> {
    override var value: T
}

suspend infix fun <T> Writable<T>.modify(action: suspend (T) -> T) {
    set(action(await()))
}

class LateInitProperty<T>() : ImmediateWritable<T>, ReadWriteProperty<Any?, T> {
    private val listeners = ArrayList<() -> Unit>()
    private var queued = ArrayList<Continuation<T>>()
    private var _value: T? = null
    override var value: T
        get() = if (ready) _value as T else throw CancelledException()
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
        else return suspendCoroutineCancellable {
            queued.add(it)
            return@suspendCoroutineCancellable {
                queued.remove(
                    it
                )
            }
        }
    }

    override fun start(): () -> Unit {
        return super.start()
    }

    override fun addListener(listener: () -> Unit): () -> Unit {
        listeners.add(listener)
        return {
            val pos = listeners.indexOfFirst { it === listener }
            if (pos != -1) {
                listeners.removeAt(pos)
            }
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = value
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}

class Property<T>(startValue: T) : ImmediateWritable<T>, ReadWriteProperty<Any?, T> {
    private val listeners = ArrayList<() -> Unit>()
    override var value: T = startValue
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
            if (pos != -1) {
                listeners.removeAt(pos)
            }
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = value
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}

class BasicListenable : Listenable {
    private val listeners = ArrayList<() -> Unit>()
    override fun addListener(listener: () -> Unit): () -> Unit {
        listeners.add(listener)
        return {
            val pos = listeners.indexOfFirst { it === listener }
            if (pos != -1) {
                listeners.removeAt(pos)
            }
        }
    }

    fun invokeAll() {
        listeners.toList().forEach { it() }
    }
}

class Constant<T>(override val value: T) : ImmediateReadable<T> {
    companion object {
        private val NOOP = {}
    }

    override fun addListener(listener: () -> Unit): () -> Unit = NOOP
    override suspend fun awaitRaw(): T = value
}

class ReactiveScopeData(
    val calculationContext: CalculationContext,
    var action: suspend () -> Unit,
    var onLoad: (() -> Unit)? = null,
) : CoroutineContext.Element {
    internal val removers: HashMap<ResourceUse, () -> Unit> = HashMap()
    internal val latestPass: ArrayList<ResourceUse> = ArrayList()
    override val key: CoroutineContext.Key<ReactiveScopeData> = Key
    internal var previousContext: CoroutineContext? = null

    internal fun run() {
        val context: CoroutineContext = EmptyCoroutineContext.childCancellation() + this
        previousContext?.cancel()
        previousContext = context
        latestPass.clear()

        var done = false
        var loadStarted = false

        action.startCoroutine(object : Continuation<Unit> {
            override val context: CoroutineContext = context

            // called when a coroutine ends. do nothing.
            override fun resumeWith(result: Result<Unit>) {
                done = true
                if (loadStarted) {
                    calculationContext.notifyLongComplete(result)
                } else {
                    calculationContext.notifyComplete(result)
                }
                if (previousContext !== context) return
                for (entry in removers.entries.toList()) {
                    if (entry.key !in latestPass) {
                        entry.value()
                        removers.remove(entry.key)
                    }
                }
            }
        })

        if (!done) {
            // start load
            loadStarted = true
            calculationContext.notifyStart()
            onLoad?.invoke()
        }
    }

    internal fun shutdown() {
        action = {}
        onLoad = {}
        removers.forEach { it.value() }
        removers.clear()
        latestPass.clear()
    }

    init {
        run()
        calculationContext.onRemove {
            shutdown()
        }
    }

    fun rerunOn(listenable: Listenable, permit: () -> Boolean) {
        if (!removers.containsKey(listenable)) {
            removers[listenable] = listenable.addListener { if (permit()) run() }
        }
        latestPass.add(listenable)
    }

    object Key : CoroutineContext.Key<ReactiveScopeData> {
        init {
            println("ReactiveScopeData V5")
        }
    }
}

fun CalculationContext.use(resourceUse: ResourceUse) {
    val x = resourceUse.start()
    onRemove { x() }
}

inline fun CalculationContext.reactiveScope(noinline action: suspend () -> Unit) = reactiveScope(null, action)
inline fun CalculationContext.reactiveScope(noinline onLoad: (() -> Unit)?, noinline action: suspend () -> Unit) {
    ReactiveScopeData(this, action, onLoad)
}

suspend inline fun rerunOn(listenable: Listenable, noinline permit: () -> Boolean = { true }) {
    coroutineContext[ReactiveScopeData.Key]?.rerunOn(listenable, permit)
}

suspend inline fun <T> ImmediateReadable<T>.await(): T {
    var permit = false
    rerunOn(this) { permit }
    val v = value
    permit = true
    return v
}

suspend inline fun <T> Readable<T>.await(): T {
    var permit = false
    rerunOn(this) { permit }
    val v = awaitRaw()
    permit = true
    return v
}

//class Ref<T>(var value: T)
//private class ReactiveScopeData(var rerun: () -> Unit) : CoroutineContext.Element {
//    val removers: HashMap<ResourceUse, () -> Unit> = HashMap()
//    val latestPass: ArrayList<ResourceUse> = ArrayList()
//    override val key: CoroutineContext.Key<ReactiveScopeData> = Key
//
//    fun shutdown() {
//        removers.forEach { it.value() }
//        removers.clear()
//        latestPass.clear()
//        rerun = {}
//    }
//
//    object Key : CoroutineContext.Key<ReactiveScopeData> {
//        init { println("ReactiveScopeData V1") }
//    }
//}
//fun CalculationContext.reactiveScope(action: suspend () -> Unit) = reactiveScope(null, action)
//fun CalculationContext.reactiveScope(onLoad: (() -> Unit)?, action: suspend () -> Unit) {
//    var run: () -> Unit = {}
//    val data = ReactiveScopeData {
//        run()
//    }
//    val name = Random.nextInt(1000000)
//    var previousContext: CoroutineContext? = null
//    var actionRef: Ref<suspend () -> Unit> = Ref(action)
//    run = run@{
//        val context: CoroutineContext = EmptyCoroutineContext.childCancellation() + data
//        previousContext?.cancel()
//        previousContext = context
//        data.latestPass.clear()
//
//        var done = false
//        var loadStarted = false
//
//        suspend { actionRef.value() }.startCoroutine(object : Continuation<Unit> {
//            override val context: CoroutineContext = context
//
//            // called when a coroutine ends. do nothing.
//            override fun resumeWith(result: Result<Unit>) {
//                done = true
//                if (loadStarted) {
//                    notifyLongComplete(result)
//                } else {
//                    notifyComplete(result)
//                }
//                if (previousContext !== context) return
//                for (entry in data.removers.entries.toList()) {
//                    if (entry.key !in data.latestPass) {
//                        entry.value()
//                        data.removers.remove(entry.key)
//                    }
//                }
//            }
//        })
//
//        if (!done) {
//            // start load
//            loadStarted = true
//            notifyStart()
//            onLoad?.invoke()
//        }
//    }
//    run()
//    this.onRemove {
//        data.shutdown()
//        run = {}
//        actionRef.value = {}
//        previousContext?.cancel()
//    }
//}
//
//suspend fun rerunOn(listenable: Listenable) {
//    coroutineContext[ReactiveScopeData.Key]?.let { data ->
//        if (!data.removers.containsKey(listenable)) {
//            data.removers[listenable] = listenable.addListener { data.rerun() }
//        }
//        data.latestPass.add(listenable)
//    }
//}
//
//suspend fun <T> Readable<T>.await(): T {
//    rerunOn(this)
//    return awaitRaw()
//}

/**
 * Desired behavior for shared:
 *
 * - Outside a reactive scope, [Readable.await] invokes the action with no sharing
 * - Inside a reactive scope, [Readable.await] starts the whole system listening and sharing the calculation.
 */
fun <T> shared(action: suspend CalculationContext.() -> T): Readable<T> {
    val removers = ArrayList<() -> Unit>()
    val ctx = object : CalculationContext {
        override fun onRemove(action: () -> Unit) {
            removers.add(action)
        }
    }
    return object : Readable<T> {
        var value: T? = null
        var ready: Boolean = false
        var exception: Exception? = null
        var listening = false
        var queued = ArrayList<Continuation<T>>()
        override suspend fun awaitRaw(): T = if (ready) {
//            println("$this: Ready answer")
            exception?.let {
//                println("rethrowing $it")
                throw it
            } ?: value as T
        } else {
//            println("$this: Already listening; queue")
            suspendCoroutineCancellable<T> {
                queued.add(it);
                startupIfNeeded()
                return@suspendCoroutineCancellable { queued.remove(it) }
            }
        }

        private fun startupIfNeeded() {
            if (!listening) {
                listening = true
                // startup
//                println("$this: Start listening")
                ctx.reactiveScope(/*onLoad = {
                    ready = false
                    // Notify that we're loading
                    listeners.toList().forEach { it() }
                }*/
                ) {
//                    if (listening) println("$this: Recalculating...")
//                    else println("$this: Starting initial calculation...")
                    val oldValue = value
                    try {
                        val result = action(ctx)
                        value = result
                        exception = null
                    } catch (e: Exception) {
                        exception = e
                    } finally {
                        ready = true
                        val listenersCopy = listeners.toList()
//                            println("$this: Initial calculation complete")
                        // This is a first result; send to our queue
                        val e = exception
                        val result = value as T
                        val copy = queued
                        queued = ArrayList()
                        copy.forEach {
                            e?.let { e -> it.resumeWithException(e) } ?: it.resume(result)
                        }
//                            println("$this: Change calculation complete, notifying")
                        // This is a change notification; notify our listeners
                        if (oldValue != value) {
                            listenersCopy.forEach { it() }
                        }
                        shutdownIfNeeded()
                    }
                }
            }
        }

        private fun shutdownIfNeeded() {
            if (listeners.isEmpty() && queued.isEmpty() && listening) {
                // shutdown
                removers.forEach { it() }
                ready = false
                listening = false
                value = null
                exception = null
            }
        }

        private val listeners = ArrayList<() -> Unit>()
        override fun addListener(listener: () -> Unit): () -> Unit {
            listeners.add(listener)
            startupIfNeeded()
            return {
                removeListener(listener)
            }
        }

        private fun removeListener(listener: () -> Unit) {
            val pos = listeners.indexOfFirst { it === listener }
            if (pos != -1) {
                listeners.removeAt(pos)
            }
            shutdownIfNeeded()
        }
    }
}

private class WaitForNotNull<T : Any>(val wraps: Readable<T?>) : Readable<T> {
    override suspend fun awaitRaw(): T = wraps.awaitNotNull()

    override fun addListener(listener: () -> Unit): () -> Unit {
        return wraps.addListener(listener)
    }

    override fun hashCode(): Int = wraps.hashCode() + 1

    override fun equals(other: Any?): Boolean = other is WaitForNotNull<*> && this.wraps == other.wraps
}

val <T : Any> Readable<T?>.waitForNotNull: Readable<T> get() = WaitForNotNull(this)
suspend fun <T : Any> Readable<T?>.awaitNotNull(): T {
    val newValue = this@awaitNotNull.awaitRaw()
    if (newValue != null) return newValue
//    val context = CalculationContext.Standard()
    return suspendCoroutineCancellable { cont ->
        var toCancel: Cancellable? = null
        val listener = {
            toCancel = launchGlobal {
                val v = this@awaitNotNull.awaitRaw()
                if (v != null) {
                    cont.resume(v)
                }
            }
        }
        val remover = addListener(listener)
        return@suspendCoroutineCancellable {
            toCancel?.cancel()
            remover()
        }
    }
}