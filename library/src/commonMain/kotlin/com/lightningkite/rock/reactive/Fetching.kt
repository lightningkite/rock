package com.lightningkite.rock.reactive

import com.lightningkite.rock.launchGlobal

class Fetching<T>(val getter: suspend () -> T) : Readable<T> {
    var loaded: Boolean = false
    var exception: Exception? = null
    var value: T? = null
    private val listeners = HashSet<() -> Unit>()

    private fun fetch() {
        launchGlobal {
            try {
                value = getter()
                loaded = true
                listeners.toList().forEach { it() }
            } catch (e: Exception) {
                exception = e
            }
        }
    }
    fun refetch() {
        loaded = false
        exception = null
        value = null
        listeners.toList().forEach { it() }
        fetch()
    }

    @Suppress("UNCHECKED_CAST")
    override val once: T
        get() = when {
            exception != null -> throw exception!!
            loaded -> value as T
            else -> throw Loading
        }

    init {
        fetch()
    }

    override fun addListener(listener: () -> Unit): () -> Unit {
        listeners.add(listener)
        return {
            listeners.remove(listener)
        }
    }

}