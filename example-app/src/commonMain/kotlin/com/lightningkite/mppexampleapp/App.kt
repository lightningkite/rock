package com.lightningkite.mppexampleapp

import com.lightningkite.rock.ExternalServices
import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.l2.*

val appTheme = Property<Theme>(MaterialLikeTheme())
fun ViewContext.app() {

//    col {
//        row {
//            button {
//                image { this.source = Icons.arrowBack.color(Color.white); description = "Go Back" }
//                onClick { navigator.goBack() }
//            }
//            h2 { ::content { navigator.currentScreen.current.title.current } } in weight(1f) in gravity(Align.Center, Align.Center)
//            externalLink {
//                image { this.source = Icons.search.color(Color.white); description = "See Code on GitHub"; newTab = true }
//                ::to { "https://github.com/lightningkite/rock/tree/main/example-app/src/commonMain/kotlin/com/lightningkite/mppexampleapp/${navigator.currentScreen.current::class.toString().removePrefix("class ")}.kt" }
//            }
//        } in important in marginless
//        navigatorView(navigator) in weight(1f) in marginless
//    } in setTheme { appTheme.current } in marginless

    appNav(AutoRoutes) {
        appName = "Rock Sample App"
        ::navItems{
            listOf(
                NavItem("Home", Icon.home, RootScreen),
                NavItem("Themes", Icon.sync, ThemesScreen),
                NavItem("Controls", Icon.settings, ControlsScreen),
                NavItem("Navigation", Icon.menu, NavigationScreen)
            )
        }
        ::currentUser{
            ProfileInfo(
                UserInfo("Test User", Icon.person.toImageSource(Color.black)),
                listOf(
                    NavItem("Root", Icon.home, RootScreen),
                    NavItem("Login", Icon.home, SampleLogInScreen),
                )
            )
        }
        actions = listOf(
            Action (
                title = "Search",
                icon = Icon.search,
                onSelect = { ExternalServices.openTab("https://github.com/lightningkite/rock/tree/main/example-app/src/commonMain/kotlin/com/lightningkite/mppexampleapp/${navigator.currentScreen.once::class.toString().removePrefix("class ")}.kt") }
            ),
            Action(
                title = "Open on GitHub",
                icon = Icon.star,
                onSelect = { ExternalServices.openTab("https://github.com/lightningkite/rock/tree/main/example-app/src/commonMain/kotlin/com/lightningkite/mppexampleapp/${navigator.currentScreen.once::class.toString().removePrefix("class ")}.kt") }
            )
        )
    } in setTheme { appTheme.current }
}
