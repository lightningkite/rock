package com.lightningkite.rock

object ViewWrapper

operator fun ViewWrapper.contains(unit: Unit): Boolean {
    return true
}

operator fun ViewWrapper.contains(boolean: Boolean): Boolean {
    return true
}
