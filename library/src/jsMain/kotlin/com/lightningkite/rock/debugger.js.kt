package com.lightningkite.rock

var debug: Boolean = false
actual fun debugger() {
    if(debug) js("debugger;")
}

actual fun gc(): GCInfo {
    return GCInfo(-1L)
}