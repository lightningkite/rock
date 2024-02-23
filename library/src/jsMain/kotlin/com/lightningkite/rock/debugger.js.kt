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

actual fun Throwable.printStackTrace2() {
    val stack = this.asDynamic().stack
    if (stack is String) {
        val error = js("Error()")
        error.name = this.toString().substringBefore(':')
        error.message = this.message?.substringAfter(':')
        error.stack = stack
        console.error(error)
    } else {
        console.log(this)
    }
}