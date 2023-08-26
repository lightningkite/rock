package com.lightningkite.mppexample

import org.w3c.dom.*


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias CheckBox = HTMLLabelElement

actual inline fun ViewContext.checkBox(setup: CheckBox.() -> Unit): Unit = element<HTMLLabelElement>("label") {
    element<HTMLInputElement>("input") {
        type = "checkbox"
    }
    setup()
}

actual fun CheckBox.bind(checked: Writable<Boolean>) {
    println(this.children[0])
    val checkbox = this.querySelector("input[type='checkbox']") as HTMLInputElement?
    if (checkbox == null) {
        println(this.children)
        println("not found")
        return
    }
    checkbox.checked = checked.once
    checkbox.addEventListener("input", {
        checked set (it.currentTarget as HTMLInputElement).checked
    })
}
