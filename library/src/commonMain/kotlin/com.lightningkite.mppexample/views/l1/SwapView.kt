package com.lightningkite.mppexample

import com.lightningkite.mppexample.NView
import com.lightningkite.mppexample.Router
import com.lightningkite.mppexample.ViewContext


expect class SwapView : NView

expect fun ViewContext.swapView(child: Readable<ViewContext.() -> Unit>): Unit
