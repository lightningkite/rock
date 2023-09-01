package com.lightningkite.mppexample


expect class RouterView : NView

expect fun ViewContext.routerView(setup: ViewContext.() -> Router): Unit
