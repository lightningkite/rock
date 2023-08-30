package com.lightningkite.mppexample

import org.w3c.dom.HTMLSpanElement


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Text = HTMLSpanElement

fun Text.sharedSetup() {
//    gravity = TextGravity.Left // adding this overrides the default button > span css that centers the text
}

actual inline fun ViewContext.text(setup: Text.() -> Unit): Unit = element<HTMLSpanElement>("span") {
    sharedSetup()
    setup()
}

actual inline fun ViewContext.h1(setup: Text.() -> Unit): Unit = element<HTMLSpanElement>("h1") {
    sharedSetup()
    setup()
}

actual inline fun ViewContext.h2(setup: Text.() -> Unit): Unit = element<HTMLSpanElement>("h2") {
    sharedSetup()
    setup()
}

actual inline fun ViewContext.h3(setup: Text.() -> Unit): Unit = element<HTMLSpanElement>("h3") {
    sharedSetup()
    setup()
}

actual inline fun ViewContext.h4(setup: Text.() -> Unit): Unit = element<HTMLSpanElement>("h4") {
    sharedSetup()
    setup()
}

actual inline fun ViewContext.h5(setup: Text.() -> Unit): Unit = element<HTMLSpanElement>("h5") {
    sharedSetup()
    setup()
}

actual inline fun ViewContext.h6(setup: Text.() -> Unit): Unit = element<HTMLSpanElement>("h6") {
    sharedSetup()
    setup()
}

actual var Text.content: String
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
