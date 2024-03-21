package com.lightningkite.rock

var debug: Boolean = true
actual fun debugger() {
}

actual fun gc(): GCInfo {
    return GCInfo(-1L)
}

actual fun assertMainThread() {
}

actual fun Throwable.printStackTrace2() {
    printStackTrace()
}