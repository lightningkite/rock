package com.lightningkite.kiteui

actual fun clockMillis(): Double {
    return System.currentTimeMillis().toDouble()
}