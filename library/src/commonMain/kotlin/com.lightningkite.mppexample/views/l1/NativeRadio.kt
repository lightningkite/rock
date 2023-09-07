package com.lightningkite.mppexample

expect class NativeRadio : NView

@ViewDsl
expect fun ViewContext.nativeRadio(setup: NativeRadio.() -> Unit = {}): Unit
expect fun NativeRadio.bind(value: String, prop: Writable<String>): Unit
expect var NativeRadio.activeColor: Color
expect var NativeRadio.activeForegroundColor: Color
