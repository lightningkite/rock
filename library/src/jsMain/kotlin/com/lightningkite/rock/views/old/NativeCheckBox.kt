package com.lightningkite.rock.views.old

import com.lightningkite.rock.models.Color
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.reactive.reactiveScope
import com.lightningkite.rock.views.ViewContext
import com.lightningkite.rock.views.ViewDsl
import org.w3c.dom.*


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NativeCheckBox = HTMLInputElement

@ViewDsl
actual inline fun ViewContext.nativeCheckBox(setup: NativeCheckBox.() -> Unit): Unit = element<HTMLInputElement>("input") {
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

actual var NativeCheckBox.disabled: Boolean
    get() = throw NotImplementedError()
    set(value) {
        this.disabled = value
    }
