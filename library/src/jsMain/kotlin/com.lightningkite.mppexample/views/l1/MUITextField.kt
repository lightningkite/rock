package com.lightningkite.mppexample

import com.lightningkite.mppexample.*
import org.w3c.dom.*


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias MUITextField = HTMLDivElement

actual inline fun ViewContext.muiTextField(setup: MUITextField.() -> Unit): Unit = element<HTMLDivElement>("div") {
    className = "rock-mui-text-field"

    textField {
        placeholder = "_" // this isn't actually displayed, but for the browser to trigger the :placeholder-shown pseudoclass it needs to be nonempty
    }

    element<HTMLLabelElement>("label") {
        innerText = ""
    }

    setup()
}

actual fun MUITextField.bind(text: Writable<String>) {
    val input = getElementsByTagName("input")[0] as TextField
    input.bind(text)
}

actual var MUITextField.label: String
    get() = throw NotImplementedError()
    set(value) {
        val label = getElementsByTagName("label")[0] as HTMLLabelElement
        label.innerText = value
    }

actual var MUITextField.textStyle: TextStyle
    get() = throw NotImplementedError()
    set(value) {
        val input = getElementsByTagName("input")[0] as TextField
        input.setStyles(value)
    }

actual var MUITextField.labelStyle: TextStyle
    get() = throw NotImplementedError()
    set(value) {
        style.setProperty("--text-field-color", value.color.toWeb()) // set the outline color to match the label color
    }

actual var MUITextField.keyboardHints: KeyboardHints
    get() = throw NotImplementedError()
    set(value) {
        val input = getElementsByTagName("input")[0] as TextField
        input.keyboardHints = value
    }