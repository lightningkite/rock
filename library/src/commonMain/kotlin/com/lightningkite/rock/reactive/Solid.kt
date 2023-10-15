package com.lightningkite.rock.reactive

import com.lightningkite.rock.views.OnRemoveHandler

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

infix fun <T> Writable<T>.bind(master: Writable<T>) {
    this.set(master.once)
    var setting = false
    master.addListener {
        if(setting) return@addListener
        setting = true
        this.set(master.once)
        setting = false
    }.also { ListeningLifecycleStack.onRemove(it) }
    this.addListener {
        if(setting) return@addListener
        setting = true
        master.set(this.once)
        setting = false
    }.also { ListeningLifecycleStack.onRemove(it) }
}