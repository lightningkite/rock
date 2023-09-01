package com.lightningkite.mppexample

import org.w3c.dom.*


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NativeButton = HTMLButtonElement

actual inline fun ViewContext.nativeButton(setup: NativeButton.() -> Unit): Unit = element<HTMLButtonElement>("button") {
    type = "submit" // may need to remove this and make it a variable
    style.display = "flex"
    style.flexDirection = "row"
    style.justifyContent = "center"
    style.alignItems = "center"
    style.height = "min-content"

    clickable = true

    setup()
}

actual fun NativeButton.onClick(action: () -> Unit) {
    addEventListener("click", {
        action()
    })
}

actual var NativeButton.clickable: Boolean
    get() = throw NotImplementedError()
    set(value) {
        disabled = !value
    }
