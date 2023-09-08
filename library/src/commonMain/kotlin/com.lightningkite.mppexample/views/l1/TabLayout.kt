package com.lightningkite.mppexample

expect class TabLayout : NView

expect fun ViewContext.tabLayout(tabs: ReactiveScope.() -> List<NavigationTab>, exists: ReactiveScope. () -> Boolean): Unit
