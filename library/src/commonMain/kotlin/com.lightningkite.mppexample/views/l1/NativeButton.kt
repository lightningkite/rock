package com.lightningkite.mppexample


expect class NativeButton : NView


expect fun ViewContext.nativeButton(setup: NativeButton.() -> Unit = {}): Unit
expect fun NativeButton.onClick(action: () -> Unit)
expect var NativeButton.clickable: Boolean
