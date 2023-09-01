package com.lightningkite.mppexample


expect class RouterView : NView

expect fun ViewContext.routerView(router: Router): Unit
