package com.lightningkite.mppexample

import org.w3c.dom.HTMLInputElement


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias TextField = HTMLInputElement

actual inline fun ViewContext.textField(setup: TextField.() -> Unit): Unit = element<HTMLInputElement>("input") {
    type = "text"
    style.width = "100%"
    setup()
}

actual fun TextField.bind(text: Writable<String>) {
    value = text.once

    reactiveScope {
        value = text.current
    }

    addEventListener("input", {
        text set it.currentTarget.asDynamic().value as String
    })
}

actual var TextField.hint: String
    get() = throw NotImplementedError()
    set(value) {
        placeholder = value
    }

actual var TextField.textStyle: TextStyle
    get() = throw NotImplementedError()
    set(value) {
        setStyles(value)
    }

actual var TextField.keyboardHints: KeyboardHints
    get() = throw NotImplementedError()
    set(value) {
//        if (value.action != null) {
//            TODO()
//        }

        type = when (value.type) {
            KeyboardType.Text -> "text"
            KeyboardType.Decimal -> "number"
            KeyboardType.Integer -> "number"
            KeyboardType.Phone -> "tel"
        }

        when (value.autocomplete) {
            AutoComplete.Email -> {
                type = "email"
                autocomplete = "email"
            }
            AutoComplete.Password -> {
                type = "password"
                autocomplete = "current-password"
            }
            AutoComplete.NewPassword -> {
                type = "password"
                autocomplete = "new-password"
            }
            AutoComplete.Phone -> {
                autocomplete = "tel"
            }
            null -> {
                autocomplete = "off"
            }
        }

//        when (value.case) {
//            KeyboardCase.None -> TODO()
//            KeyboardCase.Letters -> TODO()
//            KeyboardCase.Words -> TODO()
//            KeyboardCase.Sentences -> TODO()
//        }
    }