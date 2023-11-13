package com.lightningkite.rock.reactive

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Keeps track of the current builder's lifecycle to add listeners to.
 * TODO: Remove this in favor of context receivers when they are available
 */
object ListeningLifecycleStack {
    val stack = ArrayList<OnRemoveHandler>()
    fun current() = stack.lastOrNull() ?: throw IllegalStateException("ListeningLifecycleStack.onRemove called outside of a builder.")

    inline fun useIn(handler: OnRemoveHandler, action: () -> Unit) {
        start(handler)
        try {
            action()
        } finally {
            end(handler)
        }
    }
    inline fun start(handler: OnRemoveHandler) {
        stack.add(handler)
    }
    inline fun end(handler: OnRemoveHandler) {
        if (stack.removeLast() != handler)
            throw ConcurrentModificationException("Multiple threads have been attempting to instantiate views at the same time.")
    }
}

interface ResourceUse {
    fun start(): ()->Unit
}

interface Listenable: ResourceUse {
    val debugName: String get() = "Unknown"
    fun addListener(listener: () -> Unit): () -> Unit
    override fun start(): () -> Unit = addListener {  }
}

interface Readable<out T> : Listenable {
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

infix fun <T> Writable<T>.equalTo(value: T): Writable<Boolean> = object: Writable<Boolean> {
    override val once: Boolean
        get() = this@equalTo.once == value

    override fun addListener(listener: () -> Unit): () -> Unit = this@equalTo.addListener(listener)

    val target = value
    override fun set(value: Boolean) {
        if(value) this@equalTo.set(target)
    }
}


class WritableDelegate<T>(val writable: Writable<T>): ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T = writable.once
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = writable.set(value)
}
val <T> Writable<T>.delegate get() = WritableDelegate(this)