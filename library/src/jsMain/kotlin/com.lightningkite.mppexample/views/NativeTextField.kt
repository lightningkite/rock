package com.lightningkite.mppexample

import org.w3c.dom.HTMLInputElement


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NativeTextField = HTMLInputElement

@ViewDsl
actual inline fun ViewContext.nativeTextField(
    setup: NativeTextField.() -> Unit
): Unit =
    element<HTMLInputElement>("input") {
        type = "text"
        style.width = "100%"
        name = "UNNAMED-INPUT"
        style.outline = "none"

        setup()
    }

actual var NativeTextField.key: String
    get() = throw NotImplementedError()
    set(value) {
        name = value
    }

actual fun NativeTextField.bind(text: Writable<String>) {
    value = text.once

    reactiveScope {
        value = text.current
    }

    addEventListener("input", {
        text set it.currentTarget.asDynamic().value as String
    })
}

actual var NativeTextField.hint: String
    get() = throw NotImplementedError()
    set(value) {
        placeholder = value
    }

actual var NativeTextField.textStyle: TextStyle
    get() = throw NotImplementedError()
    set(value) {
        setStyles(value)
    }

actual var NativeTextField.minValue: Double?
    get() = throw NotImplementedError()
    set(value) {
        if (value == null)
            removeAttribute("min")
        else
            min = value.toString()
    }

actual var NativeTextField.maxValue: Double?
    get() = throw NotImplementedError()
    set(value) {
        if (value == null)
            removeAttribute("max")
        else
            max = value.toString()
    }

actual var NativeTextField.keyboardHints: KeyboardHints
    get() = throw NotImplementedError()
    set(value) {
        if (value.action != null) {
            TODO()
        }

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
actual var NativeTextField.validation: InputValidation
    get() = throw NotImplementedError()
    set(value) {
        required = value.required
        if (value.minLength == null)
            removeAttribute("minLength")
        else
            minLength = value.minLength
        if (value.maxLength == null)
            removeAttribute("maxLength")
        else
            maxLength = value.maxLength
    }
