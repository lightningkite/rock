package com.lightningkite.rock.reactive

fun <A, B> Readable<A>.distinctUntilChanged(map: (A) -> B): Readable<B> {
    return object : Readable<B> {
        override var once: B = map(this@distinctUntilChanged.once)
            private set

        override fun addListener(listener: () -> Unit): () -> Unit {
            return this@distinctUntilChanged.addListener {
                val newValue = map(this@distinctUntilChanged.once)
                if (newValue != once) {
                    once = newValue
                    listener()
                }
            }
        }
    }
}