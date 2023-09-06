package com.lightningkite.mppexample


expect class Text : NViewWithTextStyle

@ViewDsl
expect fun ViewContext.nativeText(setup: Text.() -> Unit = {}): Unit

@ViewDsl
expect fun ViewContext.nativeH1(setup: Text.() -> Unit = {}): Unit

@ViewDsl
expect fun ViewContext.nativeH2(setup: Text.() -> Unit = {}): Unit

@ViewDsl
expect fun ViewContext.nativeH3(setup: Text.() -> Unit = {}): Unit

@ViewDsl
expect fun ViewContext.nativeH4(setup: Text.() -> Unit = {}): Unit

@ViewDsl
expect fun ViewContext.nativeH5(setup: Text.() -> Unit = {}): Unit

@ViewDsl
expect fun ViewContext.nativeH6(setup: Text.() -> Unit = {}): Unit

expect var Text.content: String
expect var Text.textStyle: TextStyle
expect var Text.gravity: TextGravity
expect var Text.selectable: Boolean
