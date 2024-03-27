package com.lightningkite.kiteui

object ViewWrapper

inline operator fun ViewWrapper.contains(unit: Unit): Boolean {
    return true
}

inline operator fun ViewWrapper.contains(boolean: Boolean): Boolean {
    return true
}
