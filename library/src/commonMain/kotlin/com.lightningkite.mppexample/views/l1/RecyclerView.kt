package com.lightningkite.mppexample

expect class RecyclerView : NView

@ViewDsl
expect fun <T> ViewContext.recyclerView(
    data: List<T>,
    render: NView.(T) -> Unit,
    height: Dimension,
    itemHeight: Dimension,
): Unit
