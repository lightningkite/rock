package com.lightningkite.mppexample


expect class CheckBox : NView

@ViewDsl
expect fun ViewContext.checkBox(setup: CheckBox.() -> Unit = {}): Unit

expect fun CheckBox.bind(checked: Writable<Boolean>): Unit
