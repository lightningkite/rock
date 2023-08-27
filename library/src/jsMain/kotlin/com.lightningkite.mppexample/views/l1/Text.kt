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
        style.color = value.color.toWeb()
        style.fontSize = value.size.toString()
        style.fontFamily = value.font
        style.fontWeight = if (value.bold) "bold" else "normal"
        style.fontStyle = if (value.italic) "italic" else "normal"
        style.textTransform = if (value.allCaps) "capitalize" else "none"
        style.lineHeight = value.lineSpacingMultiplier.toString()
        style.letterSpacing = value.letterSpacing.toString()
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
