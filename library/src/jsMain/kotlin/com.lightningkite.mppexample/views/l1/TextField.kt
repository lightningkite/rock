package com.lightningkite.mppexample

import org.w3c.dom.HTMLInputElement


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias TextField = HTMLInputElement

actual inline fun ViewContext.textField(setup: TextField.() -> Unit): Unit = element<HTMLInputElement>("input") {
    type = "text"
    setup()
}

actual fun TextField.bind(text: Writable<String>) {
    value = text.once
    addEventListener("input", {
        text set it.currentTarget.asDynamic().value as String
    })
}

actual var TextField.hint: String
    get() = throw NotImplementedError()
    set(value) {
        placeholder = value
    }