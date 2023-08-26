package com.lightningkite.mppexample

import kotlinx.browser.window
import kotlin.coroutines.resume

internal actual inline fun afterTimeout(milliseconds: Long, crossinline action: () -> Unit): () -> Unit {
    val handle = window.setTimeout({ ->
        action()
    }, milliseconds.toInt())
    return {
        window.clearTimeout(handle)
    }
}