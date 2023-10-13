package com.lightningkite.rock.views.old

import com.lightningkite.rock.models.Color
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.reactive.reactiveScope
import com.lightningkite.rock.views.ViewContext
import com.lightningkite.rock.views.ViewDsl
import org.w3c.dom.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NativeRadio = HTMLInputElement

@ViewDsl
actual inline fun ViewContext.nativeRadio(setup: NativeRadio.() -> Unit): Unit = element<HTMLInputElement>("input") {
    type = "radio"
    activeColor = Color.black
    activeForegroundColor = Color.white
    setup()
}

actual fun NativeRadio.bind(value: String, prop: Writable<String>) {
    addEventListener("input", {
        prop set value
    })
    reactiveScope {
        val checkedState = prop.current == value
        checked = checkedState
    }
}

actual var NativeRadio.activeColor: Color
    get() = throw NotImplementedError()
    set(value) {
        style.setProperty("--active", value.toWeb())
    }

actual var NativeRadio.activeForegroundColor: Color
    get() = throw NotImplementedError()
    set(value) {
        style.setProperty("--active-inner", value.toWeb())
    }

actual var NativeRadio.radioDisabled: Boolean
    get() = throw NotImplementedError()
    set(value) {
        this.disabled = value
    }
