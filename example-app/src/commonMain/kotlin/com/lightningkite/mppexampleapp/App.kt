package com.lightningkite.mppexampleapp

import com.lightningkite.mppexampleapp.UseFullScreen
import com.lightningkite.rock.ExternalServices
import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.l2.*

val appTheme = Property<Theme>(MaterialLikeTheme(primary = Color(1f, 0.2f, 0.2f, 0.2f), title = FontAndStyle(font = Resources.fontsMontserrat)))
fun ViewWriter.app() {

    appNav(AutoRoutes) {
        appName = "Rock Sample App"
        ::navItems {
            listOf(
                NavItem("Home", Icon.home, RootScreen),
                NavItem("Themes", Icon.sync, ThemesScreen),
                NavItem("Controls", Icon.settings, ControlsScreen),
                NavItem("Navigation", Icon.menu, NavigationScreen),
                NavItem("Forms", Icon.done, FormsScreen)
            )
        }
        ::currentUser { UserInfo("Test User", null, Icon.person) }

        ::userLinks {
            listOf(
                NavItem("Root", Icon.home, RootScreen),
                NavItem("Log Out", Icon.home, SampleLogInScreen),
            )
        }

        ::exists {
            navigator.currentScreen.await() !is UseFullScreen
        }

        actions = listOf(
            Action(
                title = "Search",
                icon = Icon.search,
                onSelect = {}
            ),
            ExternalNav(
                title = "Open on GitHub",
                icon = Icon.star,
                to = {
                    val className = navigator.currentScreen.await()?.let { it::class.toString().removePrefix("class ") } ?: "App"
                    "https://github.com/lightningkite/rock/tree/main/example-app/src/commonMain/kotlin/com/lightningkite/mppexampleapp/$className.kt"
                }
            )
        )

    } in setTheme { appTheme.await() }
}
