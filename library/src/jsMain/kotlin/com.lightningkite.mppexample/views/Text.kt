package com.lightningkite.mppexample

import org.w3c.dom.HTMLSpanElement


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Text = HTMLSpanElement

@ViewDsl
actual inline fun ViewContext.nativeText(setup: Text.() -> Unit): Unit = element<HTMLSpanElement>("span") {
    setup()
}

@ViewDsl
actual inline fun ViewContext.nativeH1(setup: Text.() -> Unit): Unit = element<HTMLSpanElement>("h1") {
    setup()
}

@ViewDsl
actual inline fun ViewContext.nativeH2(setup: Text.() -> Unit): Unit = element<HTMLSpanElement>("h2") {
    setup()
}

@ViewDsl
actual inline fun ViewContext.nativeH3(setup: Text.() -> Unit): Unit = element<HTMLSpanElement>("h3") {
    setup()
}

@ViewDsl
actual inline fun ViewContext.nativeH4(setup: Text.() -> Unit): Unit = element<HTMLSpanElement>("h4") {
    setup()
}

@ViewDsl
actual inline fun ViewContext.nativeH5(setup: Text.() -> Unit): Unit = element<HTMLSpanElement>("h5") {
    setup()
}

@ViewDsl
actual inline fun ViewContext.nativeH6(setup: Text.() -> Unit): Unit = element<HTMLSpanElement>("h6") {
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
        style.textAlign = when (value) {
            TextGravity.Left -> "left"
            TextGravity.Center -> "center"
            TextGravity.Right -> "right"
        }
    }

actual var Text.selectable: Boolean
    get() = throw NotImplementedError()
    set(value) {
        style.setProperty("user-select", if (value) "text" else "none")
    }
