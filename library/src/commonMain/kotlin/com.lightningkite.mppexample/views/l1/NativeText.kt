package com.lightningkite.mppexample


expect class NativeText : NViewWithTextStyle

@ViewDsl
expect fun ViewContext.nativeText(setup: NativeText.() -> Unit = {}): Unit

@ViewDsl
expect fun ViewContext.nativeH1(setup: NativeText.() -> Unit = {}): Unit

@ViewDsl
expect fun ViewContext.nativeH2(setup: NativeText.() -> Unit = {}): Unit

@ViewDsl
expect fun ViewContext.nativeH3(setup: NativeText.() -> Unit = {}): Unit

@ViewDsl
expect fun ViewContext.nativeH4(setup: NativeText.() -> Unit = {}): Unit

@ViewDsl
expect fun ViewContext.nativeH5(setup: NativeText.() -> Unit = {}): Unit

@ViewDsl
expect fun ViewContext.nativeH6(setup: NativeText.() -> Unit = {}): Unit

expect var NativeText.content: String
expect var NativeText.textStyle: TextStyle
expect var NativeText.gravity: TextGravity
expect var NativeText.selectable: Boolean
