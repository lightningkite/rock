package com.lightningkite.mppexample

expect class SwapView : NView

@ViewDsl
expect fun ViewContext.swapView(child: Readable<ViewContext.() -> Unit>): Unit
