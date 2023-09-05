package com.lightningkite.mppexample

import kotlin.reflect.KMutableProperty0

/**
 * Keeps track of the current builder's lifecycle to add listeners to.
 * TODO: Remove this in favor of context receivers when they are available
 */
object ListeningLifecycleStack {
    val stack = ArrayList<OnRemoveHandler>()
    fun onRemove(action: () -> Unit) =
        if (stack.isNotEmpty()) stack.last()(action) else throw IllegalStateException("ListeningLifecycleStack.onRemove called outside of a builder.")

    inline fun useIn(noinline handler: OnRemoveHandler, action: () -> Unit) {
        stack.add(handler)
        try {
            action()
        } finally {
            if (stack.removeLast() != handler)
                throw ConcurrentModificationException("Multiple threads have been attempting to instantiate views at the same time.")
        }
    }
}

@DslMarker
annotation class ReactiveB

@ReactiveB
operator fun <T, IGNORED> ((T) -> IGNORED).invoke(actionToCalculate: ReactiveScope.() -> T) = reactiveScope {
    this@invoke(actionToCalculate(this))
}

@ReactiveB
operator fun <T> KMutableProperty0<T>.invoke(actionToCalculate: ReactiveScope.() -> T) = reactiveScope {
    this@invoke.set(actionToCalculate(this))
}

@ReactiveB
fun reactiveScope(action: ReactiveScope.() -> Unit) {
    val dm = ReactiveScope(action)
    ListeningLifecycleStack.onRemove { dm.clear() }
}

class ReactiveScope(val action: ReactiveScope.() -> Unit) {
    private val removers: HashMap<Listenable, () -> Unit> = HashMap()
    private val latestPass: HashSet<Listenable> = HashSet()
    fun rerunOn(listenable: Listenable) {
        latestPass.add(listenable)
    }

    operator fun invoke() {
        latestPass.clear()
        try {
            action()
        } finally {
            postRun()
        }
    }

    private fun postRun() {
        // Remove listeners we no longer depend on
        for (entry in removers.entries.toList()) {
            if (entry.key !in latestPass) {
                entry.value()
                removers.remove(entry.key)
            }
        }
        // Add listeners that are new
        for (new in latestPass) {
            if (!removers.containsKey(new)) {
                removers[new] = new.addListener { this() }
            }
        }
    }

    @ReactiveB
    val <T> Readable<T>.current: T
        get() {
            rerunOn(this)
            return once
        }

    fun clear() {
        removers.forEach { it.value() }
        removers.clear()
    }

    init {
        this()
    }
}

interface Listenable {
    val debugName: String get() = "Unknown"
    fun addListener(listener: () -> Unit): () -> Unit
}

interface Readable<T> : Listenable {
    override val debugName: String
        get() = "Readable whose value is $once"
    val once: T
}

interface Writable<T> : Readable<T> {
    override val debugName: String
        get() = "Writable whose value is $once"

    infix fun set(value: T)
    infix fun modify(update: (T) -> T) = set(update(once))
}

class Property<T>(startValue: T) : Writable<T> {
    override val debugName: String
        get() = "Property whose value is $once"
    private val listeners = HashSet<() -> Unit>()
    override var once: T = startValue
        private set(value) {
            field = value
            listeners.toList().forEach { it() }
        }

    override infix fun set(value: T) {
        this.once = value
    }

    override fun addListener(listener: () -> Unit): () -> Unit {
        listeners.add(listener)
        return {
            listeners.remove(listener)
        }
    }
}

class SharedReadable<T>(computer: ReactiveScope.() -> T) : Readable<T> {
    private val listeners = HashSet<()->Unit>()
    @Suppress("UNCHECKED_CAST")
    override val once: T
        get() = if(!ready) throw IllegalStateException() else currentValue as T
    private var ready: Boolean = false
    private var currentValue: T? = null
        set(value) {
            if (!ready || value != field) {
                ready = true
                field = value
                listeners.forEach { it() }
            }
        }
    val rs = ReactiveScope { currentValue = computer() }
    init {
        rs.clear()
    }

    override fun addListener(listener: () -> Unit): () -> Unit {
        if(listeners.isEmpty()) {
            // startup
            rs()
        }
        listeners.add(listener)
        return { removeListener(listener) }
    }

    private fun removeListener(listener: ()->Unit) {
        listeners.remove(listener)
        if(listeners.isEmpty()) {
            // shutdown
            rs.clear()
        }
    }
}

