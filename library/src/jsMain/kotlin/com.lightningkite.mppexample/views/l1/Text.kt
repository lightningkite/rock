package com.lightningkite.mppexample

import org.w3c.dom.HTMLSpanElement


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Text = HTMLSpanElement

actual inline fun ViewContext.text(setup: Text.() -> Unit): Unit = element<HTMLSpanElement>("span") {
    gravity = TextGravity.Left
    setup()
}
actual var Text.text: String
    get() = this.textContent ?: ""
    set(value) {
        this.textContent = value
    }

actual var Text.textStyle: TextStyle
    get() = throw NotImplementedError()
    set(value) {
        setStyles(value)
    }

actual var Text.gravity: TextGravity
    get() = throw NotImplementedError()
    set(value) {
        style.textAlign = when(value) {
            TextGravity.Left -> "left"
            TextGravity.Center -> "center"
            TextGravity.Right -> "right"
        }
    }
