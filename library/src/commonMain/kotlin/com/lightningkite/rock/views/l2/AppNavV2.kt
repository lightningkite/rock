package com.lightningkite.rock.views.l2

import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.PlatformNavigator
import com.lightningkite.rock.navigation.Routes
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

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
        dismissBackground {
            val nav = navigator.dialog
            ::exists { nav.currentScreen.await() != null }
            onClick { nav.dismiss() }
            navigatorViewDialog() in tweakTheme { it.dialog() }
        } 
    } 
}