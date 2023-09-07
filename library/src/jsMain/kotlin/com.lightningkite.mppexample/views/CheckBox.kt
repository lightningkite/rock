package com.lightningkite.mppexample

import org.w3c.dom.*


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NativeCheckBox = HTMLInputElement

@ViewDsl
actual inline fun ViewContext.nativeCheckBox(setup: NativeCheckBox.() -> Unit): Unit = element<HTMLInputElement>("input") {
    classList.add("rock-checkbox")
    type = "checkbox"
    checkedColor = Color.black
    checkedForegroundColor = Color.white
    setup()
}

actual fun NativeCheckBox.bind(checked: Writable<Boolean>) {
    addEventListener("input", {
        checked set (it.currentTarget as HTMLInputElement).checked
    })

    reactiveScope {
        val checkedState = checked.current
        this@bind.checked = checkedState
    }
}

actual var NativeCheckBox.checkedColor: Color
    get() = throw NotImplementedError()
    set(value) {
        style.setProperty("--active", value.toWeb())
    }

actual var NativeCheckBox.checkedForegroundColor: Color
    get() = throw NotImplementedError()
    set(value) {
        style.setProperty("--active-inner", value.toWeb())
    }
