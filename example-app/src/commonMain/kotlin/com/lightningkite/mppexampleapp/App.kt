package com.lightningkite.mppexampleapp

import com.lightningkite.rock.ExternalServices
import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.l2.*

val appTheme = Property<Theme>(MaterialLikeTheme())
fun ViewWriter.app() {

    appNav(AutoRoutes) {
        appName = "Rock Sample App"
        ::navItems{
            listOf(
                NavItem("Home", Icon.home, RootScreen),
                NavItem("Themes", Icon.sync, ThemesScreen),
                NavItem("Controls", Icon.settings, ControlsScreen),
                NavItem("Navigation", Icon.menu, NavigationScreen),
                NavItem("Forms", Icon.done, FormsScreen)
            )
        }
        ::currentUser{
            ProfileInfo(
                UserInfo("Test User", null, Icon.person),
            )
        }

        ::userLinks {
            listOf(
                NavItem("Root", Icon.home, RootScreen),
                NavItem("Log Out", Icon.home, SampleLogInScreen),
            )
        }
        actions = listOf(
            Action(
                title = "Search",
                icon = Icon.search,
                onSelect = {}
            ),
            Action(
                title = "Open on GitHub",
                icon = Icon.star,
                onSelect = {
                    val className = navigator.currentScreen.await()?.let { it::class.toString().removePrefix("class ") } ?: "App"
                    ExternalServices.openTab(
                        "https://github.com/lightningkite/rock/tree/main/example-app/src/commonMain/kotlin/com/lightningkite/mppexampleapp/$className.kt"
                    )
                }
            )
        )
    } in setTheme { appTheme.await() }
}
