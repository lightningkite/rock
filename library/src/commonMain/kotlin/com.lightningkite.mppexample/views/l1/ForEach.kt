package com.lightningkite.mppexample

expect class ForEach : NView

enum class ForEachDirection {
    Horizontal,
    Vertical
}

@ViewDsl
expect fun <T> ViewContext.forEach(
    data: ReactiveScope.() -> List<T>,
    render: NView.(T) -> Unit,
    fallback: NView.() -> Unit = {},
    direction: ForEachDirection = ForEachDirection.Vertical,
): Unit

@ViewDsl
expect fun <T> ViewContext.forEach(
    data: ReactiveScope.() -> List<T>,
    render: NView.(Int, T) -> Unit,
    fallback: NView.() -> Unit = {},
    direction: ForEachDirection = ForEachDirection.Vertical,
): Unit
