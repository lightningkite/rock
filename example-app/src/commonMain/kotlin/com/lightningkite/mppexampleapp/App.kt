package com.lightningkite.mppexampleapp

import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.l2.*

val appTheme = Property<Theme>(
    MaterialLikeTheme(
        primary = Color(1f, 0.2f, 0.2f, 0.2f),
        secondary = Color.blue,
        title = FontAndStyle(font = Resources.fontsMontserrat),
        body = FontAndStyle(font = Resources.fontsMontserrat),
    )
)

fun ViewWriter.app() {

    appNav(AutoRoutes) {
        appName = "Rock Sample App"
        ::navItems {
            listOf(
                NavLink(title = { "Home" }, icon = { Icon.home }, count = { 1 }) { RootScreen },
                NavLink({ "Themes" }, { Icon.sync }, count = { 0 }) { ThemesScreen },
                NavLink("Controls", Icon.settings, ControlsScreen),
                NavLink("Navigation", Icon.menu, NavigationScreen),
                NavLink("Forms", Icon.done, FormsScreen)
            )
        }

        ::exists {
            navigator.currentScreen.await() !is UseFullScreen
        }

        actions = listOf(
            NavAction(
                title = { "Search" },
                icon = { Icon.search },
                onSelect = {}
            ),
            NavExternal(
                title = { "Open on GitHub" },
                icon = { Icon.star },
                to = {
                    val className =
                        navigator.currentScreen.await()?.let { it::class.toString().removePrefix("class ") } ?: "App"
                    "https://github.com/lightningkite/rock/tree/main/example-app/src/commonMain/kotlin/com/lightningkite/mppexampleapp/$className.kt"
                }
            )
        )

    } in setTheme { appTheme.await() }
}
