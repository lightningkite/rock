package com.lightningkite.mppexample

import org.w3c.dom.HTMLSpanElement


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Text = HTMLSpanElement

fun Text.sharedSetup() {
//    gravity = TextGravity.Left // adding this overrides the default button > span css that centers the text
}

actual inline fun ViewContext.nativeText(setup: Text.() -> Unit): Unit = element<HTMLSpanElement>("span") {
    sharedSetup()
    setup()
}

actual inline fun ViewContext.nativeH1(setup: Text.() -> Unit): Unit = element<HTMLSpanElement>("h1") {
    sharedSetup()
    setup()
}

actual inline fun ViewContext.nativeH2(setup: Text.() -> Unit): Unit = element<HTMLSpanElement>("h2") {
    sharedSetup()
    setup()
}

actual inline fun ViewContext.nativeH3(setup: Text.() -> Unit): Unit = element<HTMLSpanElement>("h3") {
    sharedSetup()
    setup()
}

actual inline fun ViewContext.nativeH4(setup: Text.() -> Unit): Unit = element<HTMLSpanElement>("h4") {
    sharedSetup()
    setup()
}

actual inline fun ViewContext.nativeH5(setup: Text.() -> Unit): Unit = element<HTMLSpanElement>("h5") {
    sharedSetup()
    setup()
}

actual inline fun ViewContext.nativeH6(setup: Text.() -> Unit): Unit = element<HTMLSpanElement>("h6") {
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

actual var Text.selectable: Boolean
    get() = throw NotImplementedError()
    set(value) {
        style.setProperty("user-select", if(value) "text" else "none")
    }