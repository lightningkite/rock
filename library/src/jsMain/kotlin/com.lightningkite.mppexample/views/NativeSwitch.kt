package com.lightningkite.mppexample

import org.w3c.dom.*


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NativeSwitch = HTMLElement

@ViewDsl
actual inline fun ViewContext.nativeSwitch(setup: NativeSwitch.() -> Unit): Unit = element<HTMLInputElement>("input") {
    className = "switch"
    type = "checkbox"

    setup()
}

actual var NativeSwitch.checkedColor: Color
    get() = throw NotImplementedError()
    set(value) {
        style.setProperty("--active", value.toWeb())
    }

actual var NativeSwitch.checkedForegroundColor: Color
    get() = throw NotImplementedError()
    set(value) {
        style.setProperty("--active-inner", value.toWeb())
    }

actual fun NativeSwitch.bind(checked: Writable<Boolean>) {
    this as HTMLInputElement
    this.checked = checked.once
    addEventListener("input", {
        checked set (it.currentTarget as HTMLInputElement).checked
    })

    reactiveScope {
        val checkedState = checked.current
        this@bind.checked = checkedState
    }
}
