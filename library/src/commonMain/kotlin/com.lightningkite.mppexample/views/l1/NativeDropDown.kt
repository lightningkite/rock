package com.lightningkite.mppexample

expect class NativeDropDown : NView
typealias Spinner = NativeDropDown

@ViewDsl
expect fun <T> ViewContext.nativeDropDown(
    options: Readable<List<T>>,
    getLabel: (T) -> String,
    getKey: (T) -> String,
    prop: Writable<T>,
): Unit
