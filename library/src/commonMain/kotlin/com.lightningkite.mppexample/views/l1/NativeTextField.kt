package com.lightningkite.mppexample

expect class NativeTextField : NViewWithTextStyle

@ViewDsl
expect fun ViewContext.nativeTextField(setup: NativeTextField.() -> Unit = {}): Unit

expect fun NativeTextField.bind(text: Writable<String>): Unit
expect var NativeTextField.textStyle: TextStyle
expect var NativeTextField.keyboardHints: KeyboardHints
expect var NativeTextField.hint: String
expect var NativeTextField.key: String
expect var NativeTextField.minValue: Double?
expect var NativeTextField.maxValue: Double?
