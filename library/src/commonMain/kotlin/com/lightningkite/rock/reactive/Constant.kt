package com.lightningkite.rock.reactive

class Constant<T>(override val once: T) : Readable<T> {
    override fun addListener(listener: () -> Unit): () -> Unit = {}
}

object LoadingForever: Readable<Nothing> {
    override val once: Nothing get() = throw Loading
    override fun addListener(listener: () -> Unit): () -> Unit = {}
}