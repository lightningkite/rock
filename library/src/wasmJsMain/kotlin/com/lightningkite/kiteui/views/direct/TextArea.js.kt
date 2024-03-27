package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.models.AutoComplete
import com.lightningkite.kiteui.models.KeyboardHints
import com.lightningkite.kiteui.reactive.Writable
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.NView2
import com.lightningkite.kiteui.views.ViewWriter
import org.w3c.dom.HTMLTextAreaElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NTextArea(override val js: HTMLTextAreaElement): NView2<HTMLTextAreaElement>()

@ViewDsl
actual inline fun ViewWriter.textAreaActual(crossinline setup: TextArea.() -> Unit): Unit =
    themedElementEditable("textarea", ::NTextArea) { setup(TextArea(this)) }

actual val TextArea.content: Writable<String> get() = native.js.vprop("input", { value }, { value = it })
actual inline var TextArea.keyboardHints: KeyboardHints
    get() = TODO()
    set(value) {
        when (value.autocomplete) {
            AutoComplete.Email -> {
                native.js.autocomplete = "email"
            }

            AutoComplete.Password -> {
                native.js.autocomplete = "current-password"
            }

            AutoComplete.NewPassword -> {
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
actual inline var TextArea.hint: String
    get() = TODO()
    set(value) {}