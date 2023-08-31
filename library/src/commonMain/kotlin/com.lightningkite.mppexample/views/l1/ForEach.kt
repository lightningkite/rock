package com.lightningkite.mppexample

expect class ForEach : NView

expect fun <T> ViewContext.forEach(
    data: ReactiveScope.() -> List<T>,
    render: NView.(T) -> Unit
): Unit
