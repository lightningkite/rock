package com.lightningkite.rock

actual fun clockMillis(): Double {
    return System.currentTimeMillis().toDouble()
}