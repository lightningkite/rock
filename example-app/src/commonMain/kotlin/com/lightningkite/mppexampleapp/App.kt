package com.lightningkite.mppexampleapp

import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.PlatformNavigator
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.navigation.Routes
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.l2.navigatorView

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
        appName = "Asdf"
        ::navItems{
            listOf(
                NavItem("Root", Icons.home, RootScreen),
                NavItem("Themes", Icons.home, ThemesScreen),
                NavItem("Controls", Icons.home, ControlsScreen),
            )
        }
        ::currentUser{
            ProfileInfo(
                UserInfo("Test User", Icons.person.color(Color.black)),
                listOf(
                    NavItem("Root", Icons.home, RootScreen),
                    NavItem("Login", Icons.home, SampleLogInScreen),
                )
            )
        }
    } in setTheme { appTheme.current }
}

val booleanContent = Property(false)

data class NavItem(
    val title: String,
    val icon: ImageSource,
    val destination: RockScreen,
    val children: List<NavItem> = listOf()
)

data class ProfileInfo(
    //...
    val currentUser: UserInfo?,
    val children: List<NavItem>
)

data class UserInfo(val name: String, val profileImage: ImageVector? = null)

interface AppNav {
    var appName: String
    var navItems: List<NavItem>
    var currentUser: ProfileInfo?

    class ByProperty : AppNav {
        val appNameProperty = Property("My App")
        override var appName: String by appNameProperty.delegate
        val navItemsProperty = Property(listOf<NavItem>())
        override var navItems: List<NavItem> by navItemsProperty.delegate
        val currentUserProperty = Property<ProfileInfo?>(null)
        override var currentUser: ProfileInfo? by currentUserProperty.delegate
    }
}

fun ViewContext.appNav(routes: Routes, setup: AppNav.() -> Unit) {
    val navigator = PlatformNavigator(routes)
    this.navigator = navigator

    val appNav = AppNav.ByProperty()

    col {
        setup(appNav)
        row {
            button {
                image { this.source = Icons.arrowBack.color(Color.white); description = "Go Back" }
                onClick { navigator.goBack() }
            }
            h2 { ::content { navigator.currentScreen.current.title.current } } in weight(1f) in gravity(
                Align.Center,
                Align.Center
            )
            externalLink {
                image {
                    this.source = Icons.search.color(Color.white); description = "See Code on GitHub"; newTab = true
                }
                ::to {
                    "https://github.com/lightningkite/rock/tree/main/example-app/src/commonMain/kotlin/com/lightningkite/mppexampleapp/${
                        navigator.currentScreen.current::class.toString().removePrefix("class ")
                    }.kt"
                }
            }
        } in important in marginless
        navigatorView(navigator) in weight(1f) in marginless
    } in marginless
}

fun hamburgerNav() {
    //row {
//            h1 { ::content.invoke { appNav.appNameProperty.current } }
//            row {
//                forEachUpdating(appNav.navItemsProperty) {
//                    link {
//                        ::to { it.current.destination }
//                        text { ::content { it.current.title } }
//                    }
//                }
//            } in card
//        }
    ////stacked nav
    //    row {
//        col {
//            col {
//                forEachUpdating(appNav.navItemsProperty) {
//                    link {
//                        ::to { it.current.destination }
//                        text { ::content { it.current.title } }
//                    }
//                }
//            }
//        } in card
//        navigatorView(navigator) in weight(1f)
//    } in weight(1f)
}

fun topNav() {
    // Nav 2 top, horizontal
    //row {
//            h1 { ::content.invoke { appNav.appNameProperty.current } }
//            row {
//                forEachUpdating(appNav.navItemsProperty) {
//                    link {
//                        ::to { it.current.destination }
//                        text { ::content { it.current.title } }
//                    }
//                }
//            } in card
//        }
}

fun bottomTabsNav() {
// Nav 3 top and bottom (top)
//        row {
//            h1 { ::content.invoke { appNav.appNameProperty.current } }
//            // Nav 4 left and top - add dropdown for user info, row becomes col
//            row {
//                forEachUpdating(appNav.navItemsProperty) {
//                    link {
//                        ::to { it.current.destination }
//                        text { ::content { it.current.title } }
//                    }
//                }
//            }
//        } in card
    //Nav 3 - top and bottom (bottom/tabs)
//        row {
//            forEachUpdating(appNav.navItemsProperty) {
//                link {
//                    ::to { it.current.destination }
//                    text { ::content { it.current.title } }
//                } in weight(1f) in marginless in themeFromLast { existing ->
//                    if (navigator.currentScreen.current == it.current.destination)
//                        existing.selected()
//                    else
//                        existing
//                } in marginless
//            }
//        } in marginless
}

fun topAndLeftNav() {
// Nav 4 left and top - add dropdown for user info
//    row {
//        h1 { ::content.invoke { appNav.appNameProperty.current } }
//        row {
//            forEachUpdating(appNav.navItemsProperty) {
//                link {
//                    ::to { it.current.destination }
//                    text { ::content { it.current.title } }
//                }
//            }
//        }
//        row {
//            image {
//                source = appNav.currentUser?.currentUser?.profileImage ?: Icons.add; description = "User icon";
//            }
//            h6 { ::content.invoke { appNav.currentUser?.currentUser?.name ?: "No user information" } }
//            col {
//                dropDown {
//                    forEachUpdating(appNav.navItemsProperty) {
//                        link {
//                            ::to { it.current.destination }
//                            text { ::content { it.current.title } }
//                        }
//                    }
//                }
//            }
//        }
//    } in card
//    row {
//        col {
//            col {
//                forEachUpdating(appNav.navItemsProperty) {
//                    link {
//                        ::to { it.current.destination }
//                        text { ::content { it.current.title } }
//                    }
//                }
//            }
//        } in card
//        navigatorView(navigator) in weight(1f)
//    } in weight(1f)
}
///TO DO
//appNav()
//functions
//search, jump to, screen name, theme derivation for bar - tie nav bar styling to theme, Rock UI toggle theme, remove back arrow on root screen?
//Nav 1 - stacked nav toggle animation
//Nav 2 - update link text
//Nav 3 - text align center
//Nav 4 - pop over