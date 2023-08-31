package com.lightningkite.mppexample

object ViewWrapper
operator fun ViewWrapper.contains(unit: Unit): Boolean {
    return true
}
operator fun ViewWrapper.contains(unit: Boolean): Boolean {
    return true
}
