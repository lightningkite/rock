package com.lightningkite.rock.views.l2

import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.PlatformNavigator
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.navigation.Routes
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*


data class NavItem(
    val title: String,
    val icon: Icon,
    val destination: RockScreen,
    val children: List<NavItem> = listOf()
)

data class ProfileInfo(
    //...
    val currentUser: UserInfo?,
)

data class UserInfo(val name: String, val profileImage: ImageVector? = null, val defaultIcon: Icon)


interface AppNav {
    var appName: String
    var appIcon: Icon
    var appLogo: ImageSource
    var navItems: List<NavItem>
    var currentUser: ProfileInfo?
    var userLinks: List<NavItem>
    var actions: List<Action>


    class ByProperty : AppNav {
        val appNameProperty = Property("My App")
        override var appName: String by appNameProperty.delegate
        val appIconProperty = Property<Icon>(Icon.home)
        override var appIcon: Icon by appIconProperty.delegate
        val appLogoProperty = Property<ImageSource>(Icon.home.toImageSource(Color.white))
        override var appLogo: ImageSource by appLogoProperty.delegate
        val navItemsProperty = Property(listOf<NavItem>())
        override var navItems: List<NavItem> by navItemsProperty.delegate
        val currentUserProperty = Property<ProfileInfo?>(null)
        override var currentUser: ProfileInfo? by currentUserProperty.delegate
        val actionsProperty = Property<List<Action>>(listOf())
        override var actions: List<Action> by actionsProperty.delegate
        val userLinksProperty = Property(listOf<NavItem>())
        override var userLinks: List<NavItem> by userLinksProperty.delegate
    }
}

val search = Property("")

val ViewContext.appNavFactory by viewContextAddon<Property<ViewContext.(AppNav.() -> Unit) -> Unit>>(
    Property(
        ViewContext::appNavTopAndLeft
    )
)

fun ViewContext.appNav(routes: Routes, setup: AppNav.() -> Unit) {
    stack {
        val navigator = PlatformNavigator(routes)
        this@appNav.navigator = navigator
        swapView {
            val alt = split()
            reactiveScope {
                swap {
                    appNavFactory.current.invoke(alt, setup)
                }
            }
        } in marginless
        stack {
            val nav = navigator.dialog
            dismissBackground {
                onClick { nav.dismiss() }
            }
            ::exists { nav.currentScreen.current != null }
            navigatorViewDialog() in scrolls in card in gravity(Align.Center, Align.Center)
        }
    } in marginless
}

fun ViewContext.appNavHamburger(setup: AppNav.() -> Unit) {
    val appNav = AppNav.ByProperty()
    val booleanContent = Property(false)

    col {
// Nav 1 hamburger
        row {
            setup(appNav)
            toggleButton {
                checked bind booleanContent
                image {
                    val currentTheme = currentTheme
                    ::source { Icon.menu.toImageSource(currentTheme().foreground) }
                    description = "Open naviagation menu"
                }
            }

            h1 { ::content.invoke { appNav.appNameProperty.current } }

            button {
                image {
                    val currentTheme = currentTheme
                    ::source { Icon.arrowBack.toImageSource(currentTheme().foreground) }
                    description = "Go Back"
                    ::visible { navigator.canGoBack.current }
                }
                onClick { navigator.goBack() }
            }
            h2 { ::content.invoke { navigator.currentScreen.current?.title?.current ?: "" } } in gravity(
                Align.Center,
                Align.Center
            ) in weight(1f)
            label {
                content = "Search"
                textField {
                    content bind search
                }
            }
            row {


                forEachUpdating(appNav.actionsProperty) {
                    button {
                        image {
                            val currentTheme = currentTheme
                            ::source { it.current.icon.toImageSource(currentTheme().foreground) }
                            ::description { it.current.title }
                        }
                        onClick { it.once.onSelect() }
                    }
                }
            }
        } in bar in marginless
        row {


            text {
                col {
                    forEachUpdating(appNav.navItemsProperty) {
                        link {
                            ::to { it.current.destination }
                            text { ::content { it.current.title } }
                        } in bar
                    }.toString()
                    ::exists { booleanContent.current }
                }
            } in bar in marginless


            navigatorView(navigator) in weight(1f)
        } in weight(1f)
    } in marginless
}

fun ViewContext.appNavTop(setup: AppNav.() -> Unit) {
    val appNav = AppNav.ByProperty()
    // Nav 2 top, horizontal
    col {
        row {
            setup(appNav)
            button {
                image {
                    val currentTheme = currentTheme
                    ::source { Icon.arrowBack.toImageSource(currentTheme().foreground) }
                    description = "Go Back"
                    ::visible { navigator.canGoBack.current }
                }
                onClick { navigator.goBack() }
            }
            h2 { ::content.invoke { navigator.currentScreen.current?.title?.current ?: "" } } in gravity(
                Align.Center,
                Align.Center
            )
            row {
                forEachUpdating(appNav.navItemsProperty) {
                    link {
                        ::to { it.current.destination }
                        text { ::content { it.current.title } }
                    } in bar
                }

            } in weight(1f)
            row {
                row {
                    label {
                        content = "Search"
                        textField {
                            content bind search
                        }
                    }
                }
                row {
                    forEachUpdating(appNav.actionsProperty) {
                        button {
                            image {
                                val currentTheme = currentTheme
                                ::source { it.current.icon.toImageSource(currentTheme().foreground) }
                                ::description { it.current.title }
                            }
                            onClick { it.once.onSelect() }
                        }
                    }

                }
            }
        } in bar in marginless
        navigatorView(navigator) in weight(1f)
    } in marginless
}

fun ViewContext.appNavBottomTabs(setup: AppNav.() -> Unit) {
    val appNav = AppNav.ByProperty()
    col {
// Nav 3 top and bottom (top)
        row {
            setup(appNav)
            button {
                image {
                    val currentTheme = currentTheme
                    ::source { Icon.arrowBack.toImageSource(currentTheme().foreground) }
                    description = "Go Back"
                    ::visible { navigator.canGoBack.current }
                }
                onClick { navigator.goBack() }
            }
            h2 { ::content.invoke { navigator.currentScreen.current?.title?.current ?: "" } } in gravity(
                Align.Center,
                Align.Center
            ) in weight(1f)
            label {
                content = "Search"
                textField {
                    content bind search
                }
            }
            row {
                forEachUpdating(appNav.actionsProperty) {
                    button {
                        image {
                            val currentTheme = currentTheme
                            ::source { it.current.icon.toImageSource(currentTheme().foreground) }
                            ::description { it.current.title }
                        }
                        onClick { it.once.onSelect() }
                    }
                }
            }
        } in bar in marginless
        navigatorView(navigator) in marginless in weight(1f)
        //Nav 3 - top and bottom (bottom/tabs)
        row {
            forEachUpdating(appNav.navItemsProperty) {
                link {
                    ::to { it.current.destination }
                    col {
                        image {
                            val currentTheme = currentTheme
                            ::source { it.current.icon.toImageSource(currentTheme().foreground) }
                        } in gravity(Align.Center, Align.Center)
                        subtext { ::content { it.current.title } } in gravity(Align.Center, Align.Center)
                    }
                } in weight(1f) in marginless in themeFromLast { existing ->
                    if (navigator.currentScreen.current == it.current.destination)
                        (existing.bar() ?: existing).down()
                    else
                        existing.bar() ?: existing
                } in marginless
            }
        } in marginless
    } in marginless
}

fun ViewContext.appNavTopAndLeft(setup: AppNav.() -> Unit) {
    val appNav = AppNav.ByProperty()
    val booleanContent = Property(false)
    col {
// Nav 4 left and top - add dropdown for user info
        row {
            setup(appNav)
            button {
                image {
                    val currentTheme = currentTheme
                    ::source { Icon.arrowBack.toImageSource(currentTheme().foreground) }
                    description = "Go Back"
                    ::visible { navigator.canGoBack.current }
                }
                onClick { navigator.goBack() }
            }
            h2 { ::content.invoke { navigator.currentScreen.current?.title?.current ?: "" } } in gravity(
                Align.Center,
                Align.Center
            )
            row {
                row {
                    toggleButton {
                        checked bind booleanContent;
                        image {
                            val currentTheme = currentTheme
                            ::source {
                                appNav.currentUser?.currentUser?.profileImage ?: Icon.person.toImageSource(
                                    currentTheme().foreground
                                )
                            }
                            description = "User icon"
                        }

                    }
                    text {
                        content = appNav.currentUser?.currentUser?.name ?: "No user"
                    } in gravity(
                        Align.Center,
                        Align.Center
                    )
                } in withPadding in hasPopover {
                    col {
                        forEachUpdating(appNav.userLinksProperty) {
                            link {
                                ::to { it.current.destination }
                                text { ::content { it.current.title } }
                            } in bar
                        }
                        ::exists { booleanContent.current }
                    }
                } in card
            } in weight(1f)
            row {
                label {
                    content = "Search"
                    textField {
                        content bind search
                    }
                }
                row {
                    forEachUpdating(appNav.actionsProperty) {
                        button {
                            image {
                                val currentTheme = currentTheme
                                ::source { it.current.icon.toImageSource(currentTheme().foreground) }
                                ::description { it.current.title }
                            }
                            onClick { it.once.onSelect() }
                        }
                    }
                }
            }

        } in bar in marginless
        row {
            col {
                col {
                    forEachUpdating(appNav.navItemsProperty) {
                        link {
                            ::to { it.current.destination }
                            text { ::content { it.current.title } }
                        } in bar
                    }
                }
            } in bar in marginless
            navigatorView(navigator) in weight(1f)
        } in weight(1f)
    } in marginless
}
///TO DO
//search, jump to
//Nav 1 -
//Nav 2 -
//Nav 3 - official tab icons
//Nav 4 - user dropdown style