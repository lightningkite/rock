package com.lightningkite.mppexample

expect class RecyclerView : NView

@ViewDsl
expect fun <T> ViewContext.recyclerView(
    data: List<T>,
    render: NView.(T) -> Unit,
    estimatedItemHeight: Dimension,
): Unit
