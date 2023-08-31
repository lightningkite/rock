package com.lightningkite.mppexample

import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLLabelElement
import org.w3c.dom.get


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NativeTextField = HTMLDivElement

actual inline fun ViewContext.nativeTextField(setup: NativeTextField.() -> Unit): Unit = element<HTMLDivElement>("div") {
    element<HTMLInputElement>("input") {
        type = "text"
        style.width = "100%"
        name = "UNNAMED-INPUT"
    }

    element<HTMLLabelElement>("label") {}

    variant = TextFieldVariant.Outlined
    setup()
}

actual var NativeTextField.key: String
    get() = throw NotImplementedError()
    set(value) {
        val input = getElementsByTagName("input")[0] as HTMLInputElement
        input.name = value
    }

actual fun NativeTextField.bind(text: Writable<String>) {
    val input = getElementsByTagName("input")[0] as HTMLInputElement
    input.value = text.once

    reactiveScope {
        input.value = text.current
    }

    input.addEventListener("input", {
        text set it.currentTarget.asDynamic().value as String
    })
}

actual var NativeTextField.hint: String
    get() = throw NotImplementedError()
    set(value) {
        val input = getElementsByTagName("input")[0] as HTMLInputElement
        input.placeholder = value

        val label = getElementsByTagName("label")[0] as HTMLLabelElement
        label.innerText = value
    }

actual var NativeTextField.textStyle: TextStyle
    get() = throw NotImplementedError()
    set(value) {
        setStyles(value)
    }

actual var NativeTextField.keyboardHints: KeyboardHints
    get() = throw NotImplementedError()
    set(value) {
        if (value.action != null) {
            TODO()
        }
        val input = getElementsByTagName("input")[0] as HTMLInputElement

        input.type = when (value.type) {
            KeyboardType.Text -> "text"
            KeyboardType.Decimal -> "number"
            KeyboardType.Integer -> "number"
            KeyboardType.Phone -> "tel"
        }

        when (value.autocomplete) {
            AutoComplete.Email -> {
                input.type = "email"
                input.autocomplete = "email"
            }
            AutoComplete.Password -> {
                input.type = "password"
                input.autocomplete = "current-password"
            }
            AutoComplete.NewPassword -> {
                input.type = "password"
                input.autocomplete = "new-password"
            }
            AutoComplete.Phone -> {
                input.autocomplete = "tel"
            }
            null -> {
                input.autocomplete = "off"
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
        val input = getElementsByTagName("input")[0] as HTMLInputElement
        input.required = value.required
        if (value.minLength == null)
            input.removeAttribute("minLength")
        else
            input.minLength = value.minLength
        if (value.maxLength == null)
            input.removeAttribute("maxLength")
        else
            input.maxLength = value.maxLength
    }

actual var NativeTextField.variant: TextFieldVariant
    get() = throw NotImplementedError()
    set(value) {
        className = when(value) {
            TextFieldVariant.Unstyled -> ""
            TextFieldVariant.Outlined -> "rock-mui-text-field"
        }
    }
