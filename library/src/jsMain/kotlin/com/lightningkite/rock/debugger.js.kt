package com.lightningkite.rock

var debug: Boolean = true
actual fun debugger() {
    if(debug) js("debugger;")
}

actual fun gc(): GCInfo {
    return GCInfo(-1L)
}

actual fun assertMainThread() {
}