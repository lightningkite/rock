package com.lightningkite.mppexample


expect class Form : NView

expect fun ViewContext.form(setup: Form.() -> Unit = {}): Unit
expect fun <T : MutableMap<String, Any>> Form.bind(
    prop: Writable<T>, onSubmit: (T) -> Unit
): Unit
