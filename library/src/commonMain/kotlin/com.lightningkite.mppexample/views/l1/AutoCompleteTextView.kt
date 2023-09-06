package com.lightningkite.mppexample

expect class AutoCompleteTextView : NView

@ViewDsl
expect fun ViewContext.autoCompleteTextView(setup: AutoCompleteTextView.() -> Unit = {}): Unit

@ViewDsl
expect fun <T> AutoCompleteTextView.bind(
    options: ReactiveScope.() -> List<T>,
    getLabel: (T) -> String,
    getKey: (T) -> String,
    prop: Writable<T?>,
): Unit

expect var AutoCompleteTextView.label: String
expect var AutoCompleteTextView.textStyle: TextStyle
expect var AutoCompleteTextView.labelStyle: TextStyle
