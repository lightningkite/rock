package com.lightningkite.mppexample

expect class MUITextField : NViewWithTextStyle

expect fun ViewContext.muiTextField(setup: MUITextField.() -> Unit): Unit
expect fun MUITextField.bind(text: Writable<String>): Unit

expect var MUITextField.textStyle: TextStyle
expect var MUITextField.labelStyle: TextStyle
//expect var MUITextField.keyboardHints: KeyboardHints
expect var MUITextField.label: String
