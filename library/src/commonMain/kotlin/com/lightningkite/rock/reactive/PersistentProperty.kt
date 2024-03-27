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
) : ImmediateWritable<T>, BaseImmediateReadable<T>(defaultValue) {
    private val listeners = ArrayList<() -> Unit>()

    override var value: T
        get() = super.value
        set(value) {
            PlatformStorage.set(key, DefaultJson.encodeToString(serializer, value))
            super.value = value
        }

    override suspend infix fun set(value: T) {
        this.value = value
    }

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
                super.value = DefaultJson.decodeFromString(serializer, stored)
            } catch (e: Exception) {
            }
    }
}

inline fun <reified T> PersistentProperty(
    key: String,
    defaultValue: T
): PersistentProperty<T> = PersistentProperty(key, defaultValue, serializer())