package com.lightningkite.rock.reactive

import kotlinx.coroutines.suspendCancellableCoroutine

object Never: Readable<Nothing> {
    override suspend fun awaitRaw(): Nothing = suspendCancellableCoroutine {  }
    override fun addListener(listener: () -> Unit): () -> Unit = {}
}