package com.lightningkite.kiteui

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

actual fun Throwable.printStackTrace2() = printStackTrace()