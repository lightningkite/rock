package com.lightningkite.rock.views.direct

import com.lightningkite.rock.launchGlobal
import com.lightningkite.rock.models.Action
import com.lightningkite.rock.models.AutoComplete
import com.lightningkite.rock.models.KeyboardHints
import com.lightningkite.rock.models.KeyboardType
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.NView2
import com.lightningkite.rock.views.ViewWriter
import org.w3c.dom.HTMLInputElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NTextField(override val js: HTMLInputElement): NView2<HTMLInputElement>()

@ViewDsl
actual inline fun ViewWriter.textFieldActual(crossinline setup: TextField.() -> Unit): Unit =
    themedElementEditable("input", ::NTextField) {
        setup(TextField(this))
    }

actual val TextField.content: Writable<String> get() = native.js.vprop("input", { value }, { value = it })
actual inline var TextField.keyboardHints: KeyboardHints
    get() = TODO()
    set(value) {
        native.js.type = when (value.type) {
            KeyboardType.Text -> "text"
            KeyboardType.Decimal -> "number"
            KeyboardType.Integer -> "number"
            KeyboardType.Phone -> "tel"
            KeyboardType.Email -> "text"
        }
        native.js.inputMode = when (value.type) {
            KeyboardType.Text -> "text"
            KeyboardType.Decimal -> "decimal"
            KeyboardType.Integer -> "numeric"
            KeyboardType.Phone -> "tel"
            KeyboardType.Email -> "email"
        }

        when (value.autocomplete) {
            AutoComplete.Email -> {
                native.js.type = "email"
                native.js.autocomplete = "email"
            }

            AutoComplete.Password -> {
                native.js.type = "password"
                native.js.autocomplete = "current-password"
            }

            AutoComplete.NewPassword -> {
                native.js.type = "password"
                native.js.autocomplete = "new-password"
            }

            AutoComplete.Phone -> {
                native.js.autocomplete = "tel"
            }

            null -> {
                native.js.autocomplete = "off"
            }
        }
    }
actual var TextField.action: Action?
    get() = TODO()
    set(value) {
        native.js.onkeyup = if (value == null) null else { ev ->
            if (ev.keyCode == 13) {
                launchGlobal {
                    value.onSelect()
                }
            }
        }
    }
actual inline var TextField.hint: String
    get() = native.js.placeholder
    set(value) {
        native.js.placeholder = value
    }
actual inline var TextField.range: ClosedRange<Double>?
    get() {
        if (native.js.min.isBlank()) return null
        if (native.js.max.isBlank()) return null
        return native.js.min.toDouble()..native.js.max.toDouble()
    }
    set(value) {
        value?.let {
            native.js.min = it.start.toString()
            native.js.max = it.endInclusive.toString()
        } ?: run {
            native.js.removeAttribute("min")
            native.js.removeAttribute("max")
        }
    }