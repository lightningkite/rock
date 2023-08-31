package com.lightningkite.mppexample


expect class CheckBox : NView

expect fun ViewContext.checkBox(setup: CheckBox.() -> Unit = {}): Unit
expect fun CheckBox.bind(checked: Writable<Boolean>): Unit
