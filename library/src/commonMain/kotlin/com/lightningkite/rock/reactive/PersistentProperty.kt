package com.lightningkite.rock.reactive

import com.lightningkite.rock.PlatformStorage
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class PersistentProperty<T>(
    private val key: String,
    defaultValue: T,
    private val serializer: KSerializer<T>,
    private val overrideDebugName: String? = null
) : Writable<T> {
    override val debugName: String
        get() = overrideDebugName ?: "PersistentProperty whose value is $once"
    private val listeners = HashSet<() -> Unit>()

    var initialized = false
        private set

    override var once: T = defaultValue
        private set(value) {
            field = value
            listeners.toList().forEach { it() }
            PlatformStorage.set(key, Json.encodeToString(serializer, value))
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

    init {
        val stored = PlatformStorage.get(key)
        println("PersistentProperty $key stored as $stored")
        initialized = true
        if (stored != null)
            once = Json.decodeFromString(serializer, stored)
    }
}

inline fun <reified T> PersistentProperty(
    key: String,
    defaultValue: T,
    overrideDebugName: String? = null
) =
    PersistentProperty(key, defaultValue, serializer(), overrideDebugName)