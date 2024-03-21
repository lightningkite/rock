package com.lightningkite.rock

actual fun clockMillis(): Double = kotlinx.datetime.Clock.System.now().toEpochMilliseconds().toDouble()