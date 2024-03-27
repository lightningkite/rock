package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.launchGlobal
import com.lightningkite.kiteui.models.Action
import com.lightningkite.kiteui.models.AutoComplete
import com.lightningkite.kiteui.models.KeyboardHints
import com.lightningkite.kiteui.models.KeyboardType
import com.lightningkite.kiteui.reactive.Writable
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.ViewWriter
import org.w3c.dom.HTMLInputElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NTextField = HTMLInputElement

@ViewDsl
actual inline fun ViewWriter.textFieldActual(crossinline setup: TextField.() -> Unit): Unit =
    themedElementEditable<HTMLInputElement>("input") {
        setup(TextField(this))
    }

actual val TextField.content: Writable<String> get() = native.vprop("input", { value }, { value = it })
actual inline var TextField.keyboardHints: KeyboardHints
    get() = TODO()
    set(value) {
        native.type = when (value.type) {
            KeyboardType.Text -> "text"
            KeyboardType.Decimal -> "number"
            KeyboardType.Integer -> "number"
            KeyboardType.Phone -> "tel"
            KeyboardType.Email -> "text"
        }
        native.inputMode = when (value.type) {
            KeyboardType.Text -> "text"
            KeyboardType.Decimal -> "decimal"
            KeyboardType.Integer -> "numeric"
            KeyboardType.Phone -> "tel"
            KeyboardType.Email -> "email"
        }

        when (value.autocomplete) {
            AutoComplete.Email -> {
                native.type = "email"
                native.autocomplete = "email"
            }

            AutoComplete.Password -> {
                native.type = "password"
                native.autocomplete = "current-password"
            }

            AutoComplete.NewPassword -> {
                native.type = "password"
                native.autocomplete = "new-password"
            }

            AutoComplete.Phone -> {
                native.autocomplete = "tel"
            }

            null -> {
                native.autocomplete = "off"
            }
        }
    }
actual var TextField.action: Action?
    get() = TODO()
    set(value) {
        native.onkeyup = if (value == null) null else { ev ->
            if (ev.keyCode == 13) {
                launchGlobal {
                    value.onSelect()
                }
            }
        }
    }
actual inline var TextField.hint: String
    get() = native.placeholder
    set(value) {
        native.placeholder = value
    }
actual inline var TextField.range: ClosedRange<Double>?
    get() {
        if (native.min.isBlank()) return null
        if (native.max.isBlank()) return null
        return native.min.toDouble()..native.max.toDouble()
    }
    set(value) {
        value?.let {
            native.min = it.start.toString()
            native.max = it.endInclusive.toString()
        } ?: run {
            native.removeAttribute("min")
            native.removeAttribute("max")
        }
    }