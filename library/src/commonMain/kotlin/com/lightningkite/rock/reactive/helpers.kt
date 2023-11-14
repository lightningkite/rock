package com.lightningkite.rock.reactive

import com.lightningkite.rock.launch
import com.lightningkite.rock.launchGlobal


infix fun <T> Writable<T>.equalTo(value: T): Writable<Boolean> = object: Writable<Boolean> {
    override suspend fun awaitRaw(): Boolean = this@equalTo.awaitRaw() == value
    override fun addListener(listener: () -> Unit): () -> Unit = this@equalTo.addListener(listener)
    val target = value
    override suspend fun set(value: Boolean) {
        if(value) this@equalTo.set(target)
    }
}


infix fun <T> Writable<T>.bind(master: Writable<T>) {
    with(CalculationContextStack.current()) {
        launch { this@bind.set(master.await()) }
        var setting = false
        master.addListener {
            if (setting) return@addListener
            setting = true
            launch { this@bind.set(master.await()) }
            setting = false
        }.also { onRemove(it) }
        this@bind.addListener {
            if (setting) return@addListener
            setting = true
            launch { master.set(this@bind.await()) }
            setting = false
        }.also { onRemove(it) }
    }
}