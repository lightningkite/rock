package com.lightningkite.mppexample

expect class RouterView : NView

@ViewDsl
expect fun ViewContext.routerView(router: Router): Unit
