package com.lightningkite.mppexample

expect class NativeSwitch : NView

@ViewDsl
expect fun ViewContext.nativeSwitch(setup: NativeSwitch.() -> Unit = {}): Unit

expect fun NativeSwitch.bind(checked: Writable<Boolean>): Unit
expect var NativeSwitch.checkedColor: Color
expect var NativeSwitch.checkedForegroundColor: Color
