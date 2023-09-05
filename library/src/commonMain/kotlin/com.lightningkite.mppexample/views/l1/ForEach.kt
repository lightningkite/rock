package com.lightningkite.mppexample

expect class ForEach : NView

enum class ForEachDirection {
    Horizontal,
    Vertical
}

expect fun <T> ViewContext.forEach(
    data: ReactiveScope.() -> List<T>,
    render: NView.(T) -> Unit,
    direction: ForEachDirection = ForEachDirection.Vertical,
): Unit

expect fun <T> ViewContext.forEach(
    data: ReactiveScope.() -> List<T>,
    render: NView.(Int, T) -> Unit,
    direction: ForEachDirection = ForEachDirection.Vertical,
): Unit
