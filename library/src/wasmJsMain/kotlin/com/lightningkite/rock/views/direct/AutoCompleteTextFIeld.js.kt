package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.NView2
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.launch
import kotlinx.browser.document
import org.w3c.dom.HTMLDataListElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import kotlin.random.Random

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NAutoCompleteTextField(override val js: HTMLInputElement): NView2<HTMLInputElement>()

@ViewDsl
actual inline fun ViewWriter.autoCompleteTextFieldActual(crossinline setup: AutoCompleteTextField.() -> Unit): Unit =
    themedElementEditable("input",  ::NAutoCompleteTextField) { setup(AutoCompleteTextField(this)) }

actual val AutoCompleteTextField.content: Writable<String>
    get() = native.js.vprop("input", { value }, {
        value = it
    })
actual inline var AutoCompleteTextField.suggestions: List<String>
    get() = TODO()
    set(value) {
        val listId = native.js.getAttribute("list") ?: run {
            val newId = "datalist" + Random.nextInt(0, Int.MAX_VALUE)
            document.body!!.appendChild((document.createElement("datalist") as HTMLDataListElement).apply {
                id = newId
            })
            native.js.setAttribute("list", newId)
            newId
        }
        document.getElementById(listId)?.let { it as? HTMLElement }?.apply {
            __resetContentToOptionList(value.map { WidgetOption(it, it) }, this@suggestions.native.js.value)
        }
    }
actual inline var AutoCompleteTextField.keyboardHints: KeyboardHints
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
actual var AutoCompleteTextField.action: Action?
    get() = TODO()
    set(value) {
        native.js.onkeyup = if (value == null) null else { ev ->
            if (ev.keyCode == 13) {
                launch {
                    value.onSelect()
                }
            }
        }
    }