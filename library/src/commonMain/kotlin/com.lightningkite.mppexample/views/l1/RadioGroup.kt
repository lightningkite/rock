package com.lightningkite.mppexample


expect class RadioGroup : NView

expect fun ViewContext.radioGroup(setup: RadioGroup.() -> Unit = {}): Unit
expect fun <T> RadioGroup.bind(
    options: ReactiveScope.() -> List<T>,
    getLabel: (T) -> String,
    getKey: (T) -> String,
    prop: Writable<T>,
): Unit

