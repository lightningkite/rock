package com.lightningkite.mppexample

expect class NativeAutoComplete : NView

@ViewDsl
expect fun ViewContext.nativeAutoComplete(setup: NativeAutoComplete.() -> Unit = {}): Unit

@ViewDsl
expect fun <T> NativeAutoComplete.bind(
    options: ReactiveScope.() -> List<T>,
    getLabel: (T) -> String,
    getKey: (T) -> String,
    prop: Writable<T?>,
): Unit

expect var NativeAutoComplete.autoCompleteHint: String
expect var NativeAutoComplete.autoCompleteTextStyle: TextStyle
