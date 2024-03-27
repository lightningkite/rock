package com.lightningkite.kiteui

expect fun debugger(): Unit
data class GCInfo(val usage: Long)
expect fun gc(): GCInfo
expect fun assertMainThread()

expect fun Throwable.printStackTrace2()