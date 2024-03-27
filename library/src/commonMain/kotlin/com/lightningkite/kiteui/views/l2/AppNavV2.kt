package com.lightningkite.kiteui.views.l2

import com.lightningkite.kiteui.contains
import com.lightningkite.kiteui.models.*
import com.lightningkite.kiteui.navigation.PlatformNavigator
import com.lightningkite.kiteui.navigation.Routes
import com.lightningkite.kiteui.reactive.*
import com.lightningkite.kiteui.views.*
import com.lightningkite.kiteui.views.direct.*

fun ViewWriter.navLayout(
    appName: String = "My App",
    appIcon: Icon = Icon.star,
    appLogo: ImageSource = Icon.star.toImageSource(Color.gray),
    navItems: List<NavElement>,
    currentUser: suspend () -> UserInfo?,
    additionalSetup: CalculationContext.()->Unit
) {

}

fun ViewWriter.navBottomBar(show: Readable<Boolean> = Constant(true), navElements: suspend () -> List<NavElement>) {
    row {
        ::exists { show.await() && !SoftInputOpen.await() }
        navGroupTabs(shared { navElements() }) {}
    } 
}

fun ViewWriter.navSideBar(navElements: suspend () -> List<NavElement>) {

}

fun ViewWriter.appBase(routes: Routes, mainLayout: ContainingView.() -> Unit) {
    stack {
        rootTheme = lastTheme
        val navigator = PlatformNavigator
        PlatformNavigator.routes = routes
        this@appBase.navigator = navigator
        mainLayout()
        navigatorViewDialog() in tweakTheme { it.dialog() }
    } 
}