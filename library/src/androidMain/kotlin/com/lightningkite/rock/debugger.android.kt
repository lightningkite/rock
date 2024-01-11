package com.lightningkite.rock

actual fun debugger() {

}

actual fun gc(): GCInfo {
    return Runtime.getRuntime().run {
        gc()
        GCInfo(totalMemory() - freeMemory())
    }
}

actual fun assertMainThread() {
}