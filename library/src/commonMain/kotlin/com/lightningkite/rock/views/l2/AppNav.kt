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


data class UserInfo(
    val name: String,
    val profileImage: ImageVector? = null,
    val defaultIcon: Icon
)


interface AppNav {
    var appName: String
    var appIcon: Icon
    var appLogo: ImageSource
    var navItems: List<NavItem>
    var currentUser: UserInfo?
    var userLinks: List<NavItem>
    var actions: List<Action>
    var exists: Boolean

    class ByProperty : AppNav {
        val appNameProperty = Property("My App")
        override var appName: String by appNameProperty
        val appIconProperty = Property<Icon>(Icon.home)
        override var appIcon: Icon by appIconProperty
        val appLogoProperty = Property<ImageSource>(Icon.home.toImageSource(Color.white))
        override var appLogo: ImageSource by appLogoProperty
        val navItemsProperty = Property(listOf<NavItem>())
        override var navItems: List<NavItem> by navItemsProperty
        val currentUserProperty = Property<UserInfo?>(null)
        override var currentUser: UserInfo? by currentUserProperty
        val actionsProperty = Property<List<Action>>(listOf())
        override var actions: List<Action> by actionsProperty
        val userLinksProperty = Property(listOf<NavItem>())
        override var userLinks: List<NavItem> by userLinksProperty
        val existsProperty = Property(true)
        override var exists: Boolean by existsProperty
    }
}


val ViewWriter.appNavFactory by viewWriterAddon<Property<ViewWriter.(AppNav.() -> Unit) -> Unit>>(
    Property(
        ViewWriter::appNavBottomTabs
    )
)

fun ViewWriter.appNav(routes: Routes, setup: AppNav.() -> Unit) {
    stack {
        val navigator = PlatformNavigator(routes)
        this@appNav.navigator = navigator
        swapView {
            val alt = split()
            reactiveScope {
                val viewMaker = appNavFactory
                    .await()

                swap {
                    viewMaker.invoke(alt, setup)
                }
            }
        } in marginless
        stack {
            val nav = navigator.dialog
            dismissBackground {
                onClick { nav.dismiss() }
            } in marginless
            ::exists { nav.currentScreen.await() != null }
            navigatorViewDialog() in scrolls in dialog in gravity(Align.Center, Align.Center)
        } in marginless
    } in marginless
}

fun ViewWriter.appNavHamburger(setup: AppNav.() -> Unit) {
    val appNav = AppNav.ByProperty()
    val showMenu = Property(false)
    col {
        row {
            setup(appNav)
            toggleButton {
                checked bind showMenu; image {
                val currentTheme = currentTheme
                ::source { Icon.menu.toImageSource(currentTheme().foreground) }
                description = "Open navigation menu"
            }
            }
            h1 { ::content.invoke { appNav.appNameProperty.await() } }
            button {
                image {
                    val currentTheme = currentTheme
                    ::source { Icon.arrowBack.toImageSource(currentTheme().foreground) }
                    description = "Go Back"
                }
                ::visible { navigator.canGoBack.await() }
                onClick { navigator.goBack() }
            }
            h2 { ::content.invoke { navigator.currentScreen.await()?.title?.await() ?: "" } } in gravity(
                Align.Center,
                Align.Center
            ) in weight(1f)
            row {
                forEachUpdating(appNav.actionsProperty) {
                    button {
                        image {
                            val currentTheme = currentTheme
                            ::source { it.await().icon.toImageSource(currentTheme().foreground) }
                            ::description { it.await().title }
                        }
                        onClick { it.await().onSelect() }
                    }
                }
            }
            ::exists { appNav.existsProperty.await() }
        } in bar in marginless
        row {
            col {
                forEachUpdating(appNav.navItemsProperty) {
                    link {
                        ::to { it.await().destination }
                        text { ::content { it.await().title } }
                    } in bar
                }.toString()
                ::exists { showMenu.await() && appNav.existsProperty.await() }
            } in bar in marginless
            navigatorView(navigator) in weight(1f) in marginless
        } in weight(1f)
    } in marginless
}


fun ViewWriter.appNavTop(setup: AppNav.() -> Unit) {
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
                }
                ::visible { navigator.canGoBack.await() }
                onClick { navigator.goBack() }
            }
            h2 { ::content.invoke { navigator.currentScreen.await()?.title?.await() ?: "" } } in gravity(
                Align.Center,
                Align.Center
            )
            row {
                forEachUpdating(appNav.navItemsProperty) {
                    link {
                        ::to { it.await().destination }
                        text { ::content { it.await().title } }
                    } in bar
                }

            } in weight(1f)
            row {
                row {
                    forEachUpdating(appNav.actionsProperty) {
                        button {
                            image {
                                val currentTheme = currentTheme
                                ::source { it.await().icon.toImageSource(currentTheme().foreground) }
                                ::description { it.await().title }
                            }
                            onClick { it.await().onSelect() }
                        }
                    }

                }
            }
            ::exists { appNav.existsProperty.await() }
        } in bar in marginless
        navigatorView(navigator) in weight(1f) in marginless
    } in marginless
}

fun ViewWriter.appNavBottomTabs(setup: AppNav.() -> Unit) {
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
                }
                ::visible { navigator.canGoBack.await() }
                onClick { navigator.goBack() }
            }
            h2 { ::content.invoke { navigator.currentScreen.await()?.title?.await() ?: "" } } in gravity(
                Align.Center,
                Align.Center
            ) in weight(1f)
            row {
                forEachUpdating(appNav.actionsProperty) {
                    button {
                        image {
                            val currentTheme = currentTheme
                            ::source { it.await().icon.toImageSource(currentTheme().foreground) }
                            ::description { it.await().title }
                        }
                        onClick { it.await().onSelect() }
                    }
                }
            }
            ::exists { appNav.existsProperty.await() }
        } in bar in marginless
        navigatorView(navigator) in weight(1f) in marginless
        //Nav 3 - top and bottom (bottom/tabs)
        row {
            forEachUpdating(appNav.navItemsProperty) {
                link {
                    ::to { it.await().destination }
                    col {
                        image {
                            val currentTheme = currentTheme
                            ::source { it.await().icon.toImageSource(currentTheme().foreground) }
                        } in gravity(Align.Center, Align.Center)
                        subtext { ::content { it.await().title } } in gravity(Align.Center, Align.Center)
                    }
                } in weight(1f) in themeFromLast { existing ->
                    if (navigator.currentScreen.await() == it.await().destination)
                        (existing.bar() ?: existing).down()
                    else
                        existing.bar() ?: existing
                } in marginless
            }
            ::exists { appNav.existsProperty.await() }
        } in marginless
    } in marginless
}

fun ViewWriter.appNavTopAndLeft(setup: AppNav.() -> Unit) {
    val appNav = AppNav.ByProperty()
    col {
// Nav 4 left and top - add dropdown for user info
        row {
            setup(appNav)
            button {
                image {
                    val currentTheme = currentTheme
                    ::source { Icon.arrowBack.toImageSource(currentTheme().foreground) }
                    description = "Go Back"
                }
                ::visible { navigator.canGoBack.await() }
                onClick { navigator.goBack() }
            }
            h2 { ::content.invoke { navigator.currentScreen.await()?.title?.await() ?: "" } } in gravity(
                Align.Center,
                Align.Center
            )
            space {} in weight(1f)
            row {
                forEachUpdating(appNav.actionsProperty) {
                    button {
                        image {
                            val currentTheme = currentTheme
                            ::source { it.await().icon.toImageSource(currentTheme().foreground) }
                            ::description { it.await().title }
                        }
                        onClick { it.await().onSelect() }
                    }
                }
            }
            row {
                image {
                    val currentTheme = currentTheme
                    ::source {
                        appNav.currentUser?.profileImage ?: Icon.person.toImageSource(
                            currentTheme().foreground
                        )
                    }
                    description = "User icon"
                }
                text {
                    content = appNav.currentUser?.name ?: "No user"
                } in gravity(
                    Align.Center,
                    Align.Center
                )
            } in withDefaultPadding in hasPopover {
                col {
                    forEachUpdating(appNav.userLinksProperty) {
                        link {
                            ::to { it.await().destination }
                            text { ::content { it.await().title } }
                        }
                    }
                } in card
            }

            ::exists { appNav.existsProperty.await() }
        } in bar in marginless
        row {
            col {
                forEachUpdating(appNav.navItemsProperty) {
                    link {
                        ::to { it.await().destination }
                        text { ::content { it.await().title } }
                    }
                }

                ::exists { appNav.navItemsProperty.await().size > 1 && appNav.existsProperty.await() }
            } in marginless
            navigatorView(navigator) in weight(1f) in marginless
        } in weight(1f)
    } in marginless
}
