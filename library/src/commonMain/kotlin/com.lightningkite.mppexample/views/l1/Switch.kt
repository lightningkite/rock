package com.lightningkite.mppexample


expect class Switch : NView

expect fun ViewContext.switch(setup: Switch.() -> Unit = {}): Unit
expect fun Switch.bind(checked: Writable<Boolean>): Unit