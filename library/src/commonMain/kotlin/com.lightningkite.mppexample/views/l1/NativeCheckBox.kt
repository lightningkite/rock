package com.lightningkite.mppexample


expect class NativeCheckBox : NView

@ViewDsl
expect fun ViewContext.nativeCheckBox(setup: NativeCheckBox.() -> Unit = {}): Unit

expect fun NativeCheckBox.bind(checked: Writable<Boolean>): Unit
expect var NativeCheckBox.checkedColor: Color
expect var NativeCheckBox.checkedForegroundColor: Color
expect var NativeCheckBox.disabled: Boolean
