package com.lightningkite.rock.reactive

class Property<T>(startValue: T, private val overrideDebugName: String? = null) : Writable<T> {
    override val debugName: String
        get() = overrideDebugName ?: "Property whose value is $once"
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

