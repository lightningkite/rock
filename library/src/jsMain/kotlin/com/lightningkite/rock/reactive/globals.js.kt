package com.lightningkite.rock.reactive

actual object SoftInputOpen : Readable<Boolean> {
    override fun addListener(listener: () -> Unit): () -> Unit {
        return {}
    }

    override suspend fun awaitRaw(): Boolean = false
}