package com.lightningkite.kiteui

actual fun clockMillis(): Double = kotlinx.datetime.Clock.System.now().toEpochMilliseconds().toDouble()