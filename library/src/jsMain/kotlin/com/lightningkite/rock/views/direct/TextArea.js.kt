package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.AutoComplete
import com.lightningkite.rock.models.KeyboardHints
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import org.w3c.dom.HTMLTextAreaElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NTextArea = HTMLTextAreaElement

@ViewDsl
actual fun ViewWriter.textAreaActual(setup: TextArea.() -> Unit): Unit =
    themedElementEditable<NTextArea>("textarea") { setup(TextArea(this)) }

actual val TextArea.content: Writable<String> get() = native.vprop("input", { value }, { value = it })
actual inline var TextArea.keyboardHints: KeyboardHints
    get() = TODO()
    set(value) {
        when (value.autocomplete) {
            AutoComplete.Email -> {
                native.autocomplete = "email"
            }

            AutoComplete.Password -> {
                native.autocomplete = "current-password"
            }

            AutoComplete.NewPassword -> {
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
actual inline var TextArea.hint: String
    get() = TODO()
    set(value) {}