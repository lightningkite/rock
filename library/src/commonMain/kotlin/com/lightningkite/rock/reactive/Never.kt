package com.lightningkite.rock.reactive

import com.lightningkite.rock.suspendCoroutineCancellable

object Never: Readable<Nothing> {
    override suspend fun awaitRaw(): Nothing = suspendCoroutineCancellable { {} }
    override fun addListener(listener: () -> Unit): () -> Unit = {}
}