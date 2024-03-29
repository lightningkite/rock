package com.lightningkite.mppexampleapp

import com.lightningkite.mppexampleapp.docs.DocSearchScreen
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
        spacing = 1.rem,
    )
)

fun ViewWriter.app() {

    appNav(AutoRoutes) {
        appName = "Rock Sample App"
        ::navItems {
            listOf(
                NavLink(title = { "Home" }, icon = { Icon.home }) { RootScreen },
                NavLink({ "Themes" }, { Icon.sync }) { ThemesScreen },
                NavLink({ "Navigation" }, { Icon.chevronRight }) { NavigationTestScreen },
                NavLink(title = { "Docs" }, icon = { Icon.list }) { DocSearchScreen },
            )
        }

        ::exists {
            navigator.currentScreen.await() !is UseFullScreen
        }

        actions = listOf(
            NavLink(
                title = { "Search" },
                icon = { Icon.search },
                destination = { DocSearchScreen }
            )
        )

    } in setTheme { appTheme.await() }
}
