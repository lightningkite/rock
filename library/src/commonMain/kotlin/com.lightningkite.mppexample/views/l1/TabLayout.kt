package com.lightningkite.mppexample

expect fun ViewContext.tabLayout(tabs: ReactiveScope.() -> List<NavigationTab>, exists: ReactiveScope. () -> Boolean): Unit
