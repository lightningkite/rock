package com.lightningkite.rock

var debug: Boolean = false
actual fun debugger() {
    if(debug) js("debugger;")
}