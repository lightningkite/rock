package com.lightningkite.mppexample

@ViewDsl
expect fun <T> ViewContext.recyclerView(
    data: ReactiveScope.() -> List<T>,
    render: ViewContext.(T) -> Unit,
    estimatedItemHeightInPixels: Int,
): Unit

@ViewDsl
expect fun <T> ViewContext.recyclerView(
    data: List<T>,
    render: ViewContext.(T) -> Unit,
    estimatedItemHeightInPixels: Int,
): Unit
