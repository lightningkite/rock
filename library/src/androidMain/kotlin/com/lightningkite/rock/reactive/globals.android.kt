package com.lightningkite.rock.reactive

import com.lightningkite.rock.models.Dimension
import com.lightningkite.rock.models.WindowStatistics

actual object AnimationFrame : Listenable {
    fun frame() {
        listeners.toList().forEach { it() }
    }

    private val listeners = ArrayList<()->Unit>()
    override fun addListener(listener: () -> Unit): () -> Unit {
        listeners.add(listener)
        return { listeners.remove(listener) }
    }
}

actual object WindowInfo: Readable<WindowStatistics> {
    private val listeners = ArrayList<() -> Unit>()
    var value: WindowStatistics = WindowStatistics(Dimension(1920f), Dimension(1080f), 1f)
        set(value) {
            field = value
            listeners.toList().forEach { it() }
        }

    override suspend fun awaitRaw(): WindowStatistics = value

    override fun addListener(listener: () -> Unit): () -> Unit {
        listeners.add(listener)
        return {
            val pos = listeners.indexOfFirst { it === listener }
            if(pos != -1) {
                listeners.removeAt(pos)
            }
        }
    }
}

actual object InForeground: Readable<Boolean> {
    private val listeners = ArrayList<() -> Unit>()
    var value: Boolean = false
        set(value) {
            field = value
            listeners.toList().forEach { it() }
        }

    override suspend fun awaitRaw(): Boolean = value

    override fun addListener(listener: () -> Unit): () -> Unit {
        listeners.add(listener)
        return {
            val pos = listeners.indexOfFirst { it === listener }
            if(pos != -1) {
                listeners.removeAt(pos)
            }
        }
    }
}

actual object SoftInputOpen : Readable<Boolean> {
    private val listeners = ArrayList<() -> Unit>()
    var value: Boolean = false
        set(value) {
            field = value
            listeners.toList().forEach { it() }
        }

    override suspend fun awaitRaw(): Boolean = value

    override fun addListener(listener: () -> Unit): () -> Unit {
        listeners.add(listener)
        return {
            val pos = listeners.indexOfFirst { it === listener }
            if(pos != -1) {
                listeners.removeAt(pos)
            }
        }
    }
}