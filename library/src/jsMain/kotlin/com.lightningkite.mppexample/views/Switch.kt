package com.lightningkite.mppexample

import org.w3c.dom.*


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Switch = HTMLUnknownElement

actual inline fun ViewContext.switch(setup: Switch.() -> Unit): Unit = element<HTMLUnknownElement>("label") {
    className = "rock-mui-switch"

    element<HTMLInputElement>("input") {
        type = "checkbox"
    }

    element<HTMLDivElement>("span") {
        className = "rock-mui-switch-check"
        style.marginRight = 12.px.value
    }
    setup()
}

actual fun Switch.bind(checked: Writable<Boolean>) {
    val checkbox = this.querySelector("input[type='checkbox']") as HTMLInputElement? ?: return
//    val span = this.querySelector("span") as HTMLSpanElement? ?: return

    checkbox.checked = checked.once
    checkbox.addEventListener("input", {
        checked set (it.currentTarget as HTMLInputElement).checked
    })

    reactiveScope {
        val checkedState = checked.current
        checkbox.checked = checkedState
    }
}
