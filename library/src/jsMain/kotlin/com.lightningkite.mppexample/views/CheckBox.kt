package com.lightningkite.mppexample

import org.w3c.dom.*


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias CheckBox = HTMLLabelElement

@ViewDsl
actual inline fun ViewContext.checkBox(setup: CheckBox.() -> Unit): Unit = element<HTMLLabelElement>("label") {
    style.setProperty("user-select", "none")
    element<HTMLInputElement>("input") {
        type = "checkbox"
    }
    setup()
}

@ViewDsl
actual fun CheckBox.bind(checked: Writable<Boolean>) {
    val checkbox = this.querySelector("input[type='checkbox']") as HTMLInputElement? ?: return
    checkbox.checked = checked.once
    checkbox.addEventListener("input", {
        checked set (it.currentTarget as HTMLInputElement).checked
    })

    reactiveScope {
        val checkedState = checked.current
        checkbox.checked = checkedState
    }
}
