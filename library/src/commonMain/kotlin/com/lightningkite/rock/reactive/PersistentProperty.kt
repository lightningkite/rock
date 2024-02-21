package com.lightningkite.rock.reactive

import com.lightningkite.rock.PlatformStorage
import com.lightningkite.rock.navigation.DefaultJson
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class PersistentProperty<T>(
    private val key: String,
    defaultValue: T,
    private val serializer: KSerializer<T>,
) : Writable<T> {
    private val listeners = ArrayList<() -> Unit>()

    private var initialized = false
        private set

    private var once: T = defaultValue
        private set(value) {
            field = value
            PlatformStorage.set(key, DefaultJson.encodeToString(serializer, value))
            listeners.toList().forEach { it() }
        }

    override suspend infix fun set(value: T) {
        this.once = value
    }

    override suspend fun awaitRaw(): T = once

    override fun addListener(listener: () -> Unit): () -> Unit {
        listeners.add(listener)
        return {
            listeners.remove(listener)
        }
    }

    init {
        val stored = PlatformStorage.get(key)
        if (stored != null)
            try {
                once = DefaultJson.decodeFromString(serializer, stored)
            } catch (e: Exception) { }
        initialized = true
    }
}

inline fun <reified T> PersistentProperty(
    key: String,
    defaultValue: T
): PersistentProperty<T> = PersistentProperty(key, defaultValue, serializer())