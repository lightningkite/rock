package com.lightningkite.mppexample

import org.w3c.dom.*


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Button = HTMLButtonElement

actual inline fun ViewContext.button(setup: Button.() -> Unit): Unit = element<HTMLButtonElement>("button") {
    type = "submit" // may need to remove this and make it a variable
    style.display = "flex"
    style.flexDirection = "row"
    style.justifyContent = "center"
    style.alignItems = "center"
    style.height = "min-content"

    setup()
}

actual fun Button.onClick(action: () -> Unit) {
    addEventListener("click", {
        action()
    })
}

actual var Button.variant: ButtonVariant
    get() = throw NotImplementedError()
    set(value) {
    }

actual var Button.palette: ButtonPalette
    get() = throw NotImplementedError()
    set(value) {
    }

actual var Button.size: ButtonSize
    get() = throw NotImplementedError()
    set(value) {
    }