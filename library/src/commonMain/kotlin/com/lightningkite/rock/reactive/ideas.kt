package com.lightningkite.rock.reactive

import com.lightningkite.rock.*
import kotlin.coroutines.*
import kotlin.jvm.JvmInline
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

@JvmInline value class ReadableState<out T>(val raw: T) {
    inline val ready: Boolean get() = raw !is NotReady
    inline val success: Boolean get() = ready && raw !is ThrownException
    inline fun onSuccess(action: (T)->Unit) {
        if(raw is NotReady) return
        if(raw is ThrownException) return
        action(raw)
    }
    inline val exception: Exception? get() = (raw as? ThrownException)?.exception
    inline fun get(): T {
        if(raw is NotReady) throw NotReadyException()
        if(raw is ThrownException) throw raw.exception
        return raw
    }
    companion object {
        @Suppress("UNCHECKED_CAST")
        val notReady: ReadableState<Nothing> = ReadableState<Any?>(NotReady) as ReadableState<Nothing>
        @Suppress("UNCHECKED_CAST")
        fun <T> exception(exception: Exception) = ReadableState<Any?>(exception) as ReadableState<T>
    }
    @Suppress("UNCHECKED_CAST")
    inline fun <B> map(mapper: (T)->B): ReadableState<B> {
        if(raw is NotReady || raw is ThrownException) return raw as ReadableState<B>
        try {
            return ReadableState(mapper(raw))
        } catch(e: Exception) {
            return exception(e)
        }
    }
}
class ThrownException(val exception: Exception)
object NotReady

interface Readable<out T> : Listenable {
    val state: ReadableState<T>
}

interface Writable<T> : Readable<T> {
    suspend infix fun set(value: T)
}

interface ImmediateReadable<out T> : Readable<T> {
    val value: T
    override val state: ReadableState<T> get() = ReadableState(value)
}

interface ImmediateWritable<T> : Writable<T>, ImmediateReadable<T> {
    override var value: T
}

suspend infix fun <T> Writable<T>.modify(action: suspend (T) -> T) {
    set(action(await()))
}

class NotReadyException(message: String? = null) : IllegalStateException(message)

abstract class BaseReadable<T>(start: ReadableState<T> = ReadableState.notReady): Readable<T> {
    private val listeners = ArrayList<() -> Unit>()
    override var state: ReadableState<T> = start
        protected set(value) {
            field = value
            listeners.toList().forEach { it() }
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
}

abstract class BaseImmediateReadable<T>(start: T): ImmediateReadable<T> {
    private val listeners = ArrayList<() -> Unit>()
    override var value: T = start
        set(value) {
            field = value
            listeners.toList().forEach { it() }
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
}

class LateInitProperty<T>() : Writable<T>, ReadWriteProperty<Any?, T>, BaseReadable<T>() {
    private val listeners = ArrayList<() -> Unit>()
    var value: T
        get() = state.get()
        set(value) {
            state = ReadableState(value)
        }

    fun unset() {
        state = ReadableState.notReady
    }

    override suspend infix fun set(value: T) {
        this.value = value
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
}

class ReactiveScopeData(
    val calculationContext: CalculationContext,
    var action: suspend () -> Unit,
    var onLoad: (() -> Unit)? = null
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

    object Key : CoroutineContext.Key<ReactiveScopeData> {
        init {
            println("ReactiveScopeData V6")
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

inline fun <T> Continuation<T>.resumeState(state: ReadableState<T>) {
    state.exception?.let { resumeWithException(it) } ?: resume(state.get())
}

suspend fun rerunOn(listenable: Listenable) {
    coroutineContext[ReactiveScopeData.Key]?.let {
        if (!it.removers.containsKey(listenable)) {
            it.removers[listenable] = listenable.addListener {
                it.run()
            }
        }
        it.latestPass.add(listenable)
    }
}

suspend fun <T> Readable<T>.await(): T {
    return coroutineContext[ReactiveScopeData.Key]?.let {
        // If we're in a reactive scope,
        val state = state
        if(state.ready) {
            // and the value is ready to go, just add the listener and proceed with the value.
            rerunOn(this@await)
            state.get()
        } else {
            // otherwise, wait for the first instance of it
            suspendCoroutineCancellable { cont ->
                val listenable = this@await
                if (it.removers.containsKey(listenable)) {
                    println("Congratulations!  You've hit a weird case!  You have a readable whose state has changed to 'Not Ready' in the middle of a second read.  We're just going to abandon and try again.")
                    throw CancelledException()
                }
                var runOnce = false
                val remover = listenable.addListener {
                    // The first time the listener runs, resume.  After that, rerun the whole scope.
                    val state = this@await.state
                    if(state.ready) {
                        if(runOnce) it.run()
                        else {
                            runOnce = true
                            cont.resumeState(state)
                        }
                    }
                }
                it.latestPass.add(listenable)
                it.removers[listenable] = remover
                return@suspendCoroutineCancellable remover
            }
        }
    } ?: awaitOnce()
}

@Deprecated("Replace with 'awaitOnce'", ReplaceWith("this.awaitOnce()", "com.lightningkite.rock.reactive.awaitOnce"))
suspend fun <T> Readable<T>.awaitRaw(): T = awaitOnce()

suspend fun <T> Readable<T>.awaitOnce(): T {
    val state = state
    return if(state.ready) state.get()
    else suspendCoroutineCancellable {
        // If it's not ready, we need to wait until it is then never bother with this again.
        var remover: (()->Unit)? = null
        var alreadyRun = false
        remover = addListener {
            val state = this.state
            if(state.ready) {
                it.resumeState(state)
                remover?.invoke() ?: run {
                    alreadyRun = true
                }
            }
        }
        if(alreadyRun) remover.invoke()
        return@suspendCoroutineCancellable remover
    }
}

fun <T> Readable<Readable<T>>.flatten(): Readable<T> {
    val first = shared { this@flatten.await() }
    return shared { first.await().await() }
}

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
        override var state: ReadableState<T> = ReadableState.notReady
        var listening = false

        private val listeners = ArrayList<() -> Unit>()

        private fun startupIfNeeded() {
            if (listening) return
            listening = true
            ctx.reactiveScope {
                try {
                    val result = ReadableState(action(ctx))
                    if (result == state) return@reactiveScope
                    state = result
                } catch (e: Exception) {
                    state = ReadableState.exception(e)
                }
                listeners.toList().forEach {
                    try {
                        it()
                    } catch (e: Exception) {
                        e.printStackTrace2()
                    }
                }
                if(this.listeners.isEmpty()) {
                    shutdownIfNotNeeded()
                }
            }
        }

        private fun shutdownIfNotNeeded() {
            if(listeners.isNotEmpty()) return
            if (!listening) return
            listening = false
            removers.forEach {
                try {
                    it()
                } catch (e: Exception) {
                    e.printStackTrace2()
                }
            }
            state = ReadableState.notReady
        }

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
                shutdownIfNotNeeded()
            }
        }
    }
}

private class WaitForNotNull<T : Any>(val wraps: Readable<T?>) : Readable<T> {

    @Suppress("UNCHECKED_CAST")
    override val state: ReadableState<T>
        get() = if(wraps.state.raw == null) ReadableState.notReady else wraps.state as ReadableState<T>

    override fun addListener(listener: () -> Unit): () -> Unit {
        return wraps.addListener(listener)
    }

    override fun hashCode(): Int = wraps.hashCode() + 1

    override fun equals(other: Any?): Boolean = other is WaitForNotNull<*> && this.wraps == other.wraps
}

val <T : Any> Readable<T?>.waitForNotNull: Readable<T> get() = WaitForNotNull(this)
suspend fun <T : Any> Readable<T?>.awaitNotNull(): T {
    val basis = await()
    if (basis == null) return suspendCoroutineCancellable<T> { {} }
    else return basis
}
