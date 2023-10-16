package com.lightningkite.rock.reactive

class SharedReadable<T>(computer: ReactiveScope.() -> T) : Readable<T> {
    private val listeners = HashSet<() -> Unit>()

    @Suppress("UNCHECKED_CAST")
    override val once: T
        get() = if (!ready) throw IllegalStateException() else currentValue as T
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
        if (listeners.isEmpty()) {
            // startup
            rs()
        }
        listeners.add(listener)
        return { removeListener(listener) }
    }

    private fun removeListener(listener: () -> Unit) {
        listeners.remove(listener)
        if (listeners.isEmpty()) {
            // shutdown
            rs.clear()
        }
    }
}

fun <T> Readable<T>.withWrite(action: (T)->Unit): Writable<T> = object: Writable<T>, Readable<T> by this {
    override fun set(value: T) = action(value)
}