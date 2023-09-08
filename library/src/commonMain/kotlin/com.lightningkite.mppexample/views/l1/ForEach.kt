package com.lightningkite.mppexample

expect class ForEach : NView

@ViewDsl
expect fun <T> ViewContext.forEach(
    data: ReactiveScope.() -> List<T>,
    render: ViewContext.(T) -> Unit,
    separator: (ViewContext.() -> Unit)? = null,
    fallback: ViewContext.() -> Unit = {},
): Unit

@ViewDsl
expect fun <T> ViewContext.forEachIndexed(
    data: ReactiveScope.() -> List<T>,
    render: ViewContext.(Int, T) -> Unit,
    separator: (ViewContext.() -> Unit)? = null,
    fallback: ViewContext.() -> Unit = {},
): Unit
