package com.lightningkite.mppexample

import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLParagraphElement


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias CheckBox = HTMLInputElement

actual inline fun ViewContext.checkBox(setup: EditText.() -> Unit): Unit = element<HTMLInputElement>("input") {
    type = "checkbox"
    setup()
}

actual fun CheckBox.bind(checked: Writable<Boolean>) {
    this.checked = checked.once
    addEventListener("input", {
        checked set (it.currentTarget as HTMLInputElement).checked
    })
}
