package com.lightningkite.mppexample

expect class NativeTextField : NViewWithTextStyle

expect fun ViewContext.nativeTextField(setup: NativeTextField.() -> Unit = {}): Unit
expect fun NativeTextField.bind(text: Writable<String>): Unit

expect var NativeTextField.textStyle: TextStyle
expect var NativeTextField.keyboardHints: KeyboardHints
expect var NativeTextField.hint: String
expect var NativeTextField.validation: InputValidation
expect var NativeTextField.key: String
expect var NativeTextField.variant: TextFieldVariant
