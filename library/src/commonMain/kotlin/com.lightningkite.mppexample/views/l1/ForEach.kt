package com.lightningkite.mppexample

expect class ForEach : NView

@ViewDsl
expect fun <T> ViewContext.forEach(
    data: ReactiveScope.() -> List<T>,
    render: NView.(T) -> Unit,
    separator: (NView.() -> Unit)? = null,
    fallback: NView.() -> Unit = {},
): Unit

@ViewDsl
expect fun <T> ViewContext.forEach(
    data: ReactiveScope.() -> List<T>,
    render: NView.(Int, T) -> Unit,
    separator: (NView.() -> Unit)? = null,
    fallback: NView.() -> Unit = {},
): Unit
