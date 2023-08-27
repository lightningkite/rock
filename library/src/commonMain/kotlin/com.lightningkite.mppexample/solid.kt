package com.lightningkite.mppexample

import kotlin.reflect.KMutableProperty0

/**
 * Keeps track of the current builder's lifecycle to add listeners to.
 * TODO: Remove this in favor of context receivers when they are available
 */
object ListeningLifecycleStack {
    val stack = ArrayList<OnRemoveHandler>()
    fun onRemove(action: () -> Unit) = stack.last()(action)
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

    private operator fun invoke() {
        latestPass.clear()
        try {
            action()
        } finally {
            // Remove listeners we no longer depend on
            for (entry in removers.entries.toList()) {
                if (entry.key !in latestPass) {
                    entry.value()
                }
            }
            // Add listeners that are new
            for (new in latestPass) {
                if (!removers.containsKey(new)) {
                    removers[new] = new.addListener { this() }
                }
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
    fun addListener(listener: () -> Unit): () -> Unit
}

interface Readable<T> : Listenable {
    val once: T
}

interface Writable<T> : Readable<T> {
    infix fun set(value: T)
    infix fun modify(update: (T)->T) = set(update(once))
}

class Property<T>(startValue: T) : Writable<T> {
    private val listeners = HashSet<() -> Unit>()
    override var once: T = startValue
        private set(value) {
            field = value
            listeners.toList().forEach { it() }
        }

    override infix fun set(value: T) { this.once = value }
    override fun addListener(listener: () -> Unit): () -> Unit {
        listeners.add(listener)
        return {
            listeners.remove(listener)
        }
    }
}
