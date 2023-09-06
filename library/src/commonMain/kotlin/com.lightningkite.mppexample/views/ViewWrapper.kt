package com.lightningkite.mppexample

object ViewWrapper

operator fun ViewWrapper.contains(unit: Unit): Boolean {
    return true
}

operator fun ViewWrapper.contains(unit: Boolean): Boolean {
    return true
}

fun wrapIf(condition: Boolean, wrapper: () -> Unit): ViewWrapper {
    if (condition) wrapper()
    return ViewWrapper
}

fun wrapIfElse(condition: Boolean, wrapper: () -> Unit, elseWrapper: () -> Unit): ViewWrapper {
    if (condition) wrapper()
    else elseWrapper()
    return ViewWrapper
}
