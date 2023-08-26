package com.lightningkite.mppexample

import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLParagraphElement


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias EditText = HTMLInputElement

actual inline fun ViewContext.editText(setup: EditText.() -> Unit): Unit = element<HTMLInputElement>("input") {
    type = "text"
    setup()
}

actual fun EditText.bind(text: Writable<String>) {
    value = text.once
    addEventListener("input", {
        text set it.currentTarget.asDynamic().value as String
    })
}

actual var EditText.hint: String
    get() = throw NotImplementedError()
    set(value) {
        placeholder = value
    }