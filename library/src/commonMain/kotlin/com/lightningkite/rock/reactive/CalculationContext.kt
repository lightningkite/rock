package com.lightningkite.rock.reactive

import kotlin.reflect.KMutableProperty0

interface CalculationContext {
    fun notifyStart()
    fun notifySuccess()
    fun notifyFailure()
    fun onRemove(action: () -> Unit)
}

object CalculationContextStack {
    val stack = ArrayList<CalculationContext>()
    fun current() = stack.lastOrNull() ?: throw IllegalStateException("CalculationContextStack.onRemove called outside of a builder.")

    inline fun useIn(handler: CalculationContext, action: () -> Unit) {
        start(handler)
        try {
            action()
        } finally {
            end(handler)
        }
    }
    inline fun start(handler: CalculationContext) {
        stack.add(handler)
    }
    inline fun end(handler: CalculationContext) {
        if (stack.removeLast() != handler)
            throw ConcurrentModificationException("Multiple threads have been attempting to instantiate views at the same time.")
    }
}

@DslMarker
annotation class ReactiveB

@ReactiveB
operator fun <T, IGNORED> ((T) -> IGNORED).invoke(actionToCalculate: suspend () -> T) = CalculationContextStack.current().reactiveScope {
    this@invoke(actionToCalculate())
}

@ReactiveB
operator fun <T> KMutableProperty0<T>.invoke(actionToCalculate: suspend () -> T) = CalculationContextStack.current().reactiveScope {
    this@invoke.set(actionToCalculate())
}