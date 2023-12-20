package com.lightningkite.rock

import java.util.*
import kotlin.concurrent.schedule

internal actual fun afterTimeout(milliseconds: Long, action: () -> Unit): () -> Unit {
    return { Timer().schedule(milliseconds) {
        action()
    } }
}