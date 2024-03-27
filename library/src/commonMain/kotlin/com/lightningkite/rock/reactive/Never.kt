package com.lightningkite.rock.reactive

import com.lightningkite.rock.suspendCoroutineCancellable

object Never: Readable<Nothing> {
    override val state: ReadableState<Nothing> get() = ReadableState.notReady
    override fun addListener(listener: () -> Unit): () -> Unit = {}
}