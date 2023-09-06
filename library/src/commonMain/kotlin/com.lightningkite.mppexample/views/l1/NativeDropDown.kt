package com.lightningkite.mppexample

expect class NativeDropDown : NView
typealias Spinner = NativeDropDown

@ViewDsl
expect fun ViewContext.nativeDropDown(setup: NativeDropDown.() -> Unit = {}): Unit

expect fun <T> NativeDropDown.bind(
    options: ReactiveScope.() -> List<T>,
    getLabel: (T) -> String,
    getKey: (T) -> String,
    prop: Writable<T?>,
): Unit
