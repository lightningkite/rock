package com.lightningkite.rock.reactive

import kotlin.reflect.KMutableProperty0

@DslMarker
annotation class ReactiveB

@ReactiveB
operator fun <T, IGNORED> ((T) -> IGNORED).invoke(actionToCalculate: ReactiveScope.() -> T) = reactiveScope {
    this@invoke(actionToCalculate(this))
}

@ReactiveB
operator fun <T> KMutableProperty0<T>.invoke(actionToCalculate: ReactiveScope.() -> T) = reactiveScope {
    this@invoke.set(actionToCalculate(this))
}

@ReactiveB
fun reactiveScope(action: ReactiveScope.() -> Unit) {
    val current = ListeningLifecycleStack.current()
    val dm = ReactiveScope {
        current.onTry()
        try {
            action()
            current.onOk()
        } catch(l: Loading) {
            current.onLoading()
        } catch(e: Exception) {
            current.onFail()
        }
    }
    current.onRemove { dm.clearScopeListeners() }
}

@ReactiveB
infix fun <T> Writable<T>.bind(master: Writable<T>) {
    this.set(master.once)
    var setting = false
    master.addListener {
        if(setting) return@addListener
        setting = true
        this.set(master.once)
        setting = false
    }.also { ListeningLifecycleStack.current().onRemove(it) }
    this.addListener {
        if(setting) return@addListener
        setting = true
        master.set(this.once)
        setting = false
    }.also { ListeningLifecycleStack.current().onRemove(it) }
}

class ReactiveScope(val action: ReactiveScope.() -> Unit) {
    private val removers: HashMap<ResourceUse, () -> Unit> = HashMap()
    private val latestPass: HashSet<ResourceUse> = HashSet()

    @ReactiveB
    fun use(resourceUse: ResourceUse) {
        if (!removers.containsKey(resourceUse)) {
            removers[resourceUse] = resourceUse.start()
        }
        latestPass.add(resourceUse)
    }

    @ReactiveB
    fun rerunOn(listenable: Listenable) {
        if (!removers.containsKey(listenable)) {
            removers[listenable] = listenable.addListener { this() }
        }
        latestPass.add(listenable)
    }

    var queueRerun: Boolean = false
    var isRunning: Boolean = false
    operator fun invoke() {
        if (isRunning) {
            queueRerun = true
            return
        }
        isRunning = true
        latestPass.clear()
        try {
            action()
        } finally {
            // Remove listeners we no longer depend on
            for (entry in removers.entries.toList()) {
                if (entry.key !in latestPass) {
                    entry.value()
                    removers.remove(entry.key)
                }
            }
            isRunning = false

            if (queueRerun) {
                queueRerun = false
                this()
            }
        }
    }

    @ReactiveB
    val <T> Readable<T>.current: T
        get() {
            rerunOn(this)
            return once
        }

    fun clearScopeListeners() {
        removers.forEach { it.value() }
        removers.clear()
    }

    init {
        this()
    }
}