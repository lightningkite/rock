package com.lightningkite.kiteui.reactive

class Constant<T>(override val value: T) : ImmediateReadable<T> {
    companion object {
        private val NOOP = {}
    }

    override fun addListener(listener: () -> Unit): () -> Unit = NOOP
}