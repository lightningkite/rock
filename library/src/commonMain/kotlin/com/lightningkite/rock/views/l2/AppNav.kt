package com.lightningkite.rock.views.l2

import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.PlatformNavigator
import com.lightningkite.rock.navigation.Routes
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

data class UserInfo(
    val name: String,
    val profileImage: ImageVector? = null,
    val defaultIcon: Icon,
)

interface AppNav {
    var appName: String
    var appIcon: Icon
    var appLogo: ImageSource
    var navItems: List<NavElement>
    var currentUser: UserInfo?
    var userLinks: List<NavElement>
    var actions: List<NavElement>
    var exists: Boolean

    class ByProperty : AppNav {
        val appNameProperty = Property("My App")
        override var appName: String by appNameProperty
        val appIconProperty = Property<Icon>(Icon.home)
        override var appIcon: Icon by appIconProperty
        val appLogoProperty = Property<ImageSource>(Icon.home.toImageSource(Color.white))
        override var appLogo: ImageSource by appLogoProperty
        val navItemsProperty = Property(listOf<NavElement>())
        override var navItems: List<NavElement> by navItemsProperty
        val currentUserProperty = Property<UserInfo?>(null)
        override var currentUser: UserInfo? by currentUserProperty
        val actionsProperty = Property<List<NavElement>>(listOf())
        override var actions: List<NavElement> by actionsProperty
        val userLinksProperty = Property(listOf<NavElement>())
        override var userLinks: List<NavElement> by userLinksProperty
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
        println("Referencing PlatformNavigator")
        val navigator = PlatformNavigator
        println("Initializing routes")
        PlatformNavigator.routes = routes
        this@appNav.navigator = navigator
        swapView {
            swapping(
                current = { appNavFactory.await() },
                views = { it(this, setup) in marginless }
            )
        } in marginless
        dismissBackground {
            val nav = navigator.dialog
            ::exists { nav.currentScreen.await() != null }
            onClick { nav.dismiss() }
            navigatorViewDialog() in tweakTheme { it.dialog() }
        } in marginless
    } in marginless
}

private fun ViewWriter.navGroupColumn(readable: Readable<List<NavElement>>) {
    forEach(readable) {
        when (it) {
            is Action -> button {
                text { ::content { it.title } }
                onClick { it.onSelect() }
            }

            is ExternalNav -> externalLink {
                ::to { it.to() }
                text { ::content { it.title } }
            }

            is NavGroup -> {
                col {
                    h3(it.title)
                    row {
                        space()
                        col {
                            navGroupColumn(shared { it.children() })
                        }
                    }
                }
            }

            is NavItem -> link {
                ::to { it.destination() }
                text { ::content { it.title } }
            } in maybeThemeFromLast { existing ->
                if (navigator.currentScreen.await()
                        ?.let { navigator.routes.render(it) } == navigator.routes.render(it.destination())
                )
                    existing.down()
                else
                    null
            }
        }
    }
}

private fun ViewWriter.navGroupActions(readable: Readable<List<NavElement>>) {
    forEach(readable) {
        when (it) {
            is Action -> button {
//                text { ::content { it.title } }
                icon(it.icon, it.title)
                onClick { it.onSelect() }
            }

            is ExternalNav -> externalLink {
                ::to { it.to() }
//                text { ::content { it.title } }
                icon(it.icon, it.title)
            }

            is NavGroup -> {
                row {
                    navGroupActions(shared { it.children() })
                }
            }

            is NavItem -> link {
                ::to { it.destination() }
//                text { ::content { it.title } }
                icon(it.icon, it.title)
            } in maybeThemeFromLast { existing ->
                if (navigator.currentScreen.await()
                        ?.let { navigator.routes.render(it) } == navigator.routes.render(it.destination())
                )
                    existing.down()
                else
                    null
            }
        }
    }
}

fun ViewWriter.navGroupTabs(readable: Readable<List<NavElement>>) {
    forEach(readable) {
        fun display(navElement: NavElement) {
            compact - col {
                image {
                    val currentTheme = currentTheme
                    ::source { it.icon.toImageSource(currentTheme().foreground) }
                } in gravity(Align.Center, Align.Center)
                subtext { ::content { it.title } } in gravity(Align.Center, Align.Center)
            }
        }
        when (it) {
            is Action -> button {
                display(it)
                onClick { it.onSelect() }
            }

            is ExternalNav -> externalLink {
                ::to { it.to() }
                display(it)
            }

            is NavGroup -> button {
                display(it)
                onClick { }  // TODO: select dialog
            }

            is NavItem -> {
                link {
                    ::to { it.destination() }
                    display(it)
                } in themeFromLast { existing ->
                    if (navigator.currentScreen.await()?.let { navigator.routes.render(it) } == navigator.routes.render(
                            it.destination()
                        ))
                        (existing.bar() ?: existing).down()
                    else
                        existing.bar() ?: existing
                }
                Unit
            }
        } in weight(1f) in marginless
    }
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
                navGroupActions(appNav.actionsProperty)
            } in withDefaultPadding
            ::exists { appNav.existsProperty.await() }
        } in bar in marginless
        row {
            col {
                navGroupColumn(appNav.navItemsProperty)
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
                navGroupActions(appNav.actionsProperty)
            } in weight(1f)
            row {
                fun renderGroup(readable: Readable<List<NavElement>>) {
                    forEach(readable) {
                        when (it) {
                            is Action -> button {
                                text { ::content { it.title } }
                                onClick { it.onSelect() }
                            }

                            is ExternalNav -> externalLink {
                                ::to { it.to() }
                                text { ::content { it.title } }
                            } in bar

                            is NavGroup -> {
                                col {
                                    h3(it.title)
                                    row {
                                        space()
                                        col {
                                            renderGroup(shared { it.children() })
                                        }
                                    }
                                }
                            }

                            is NavItem -> link {
                                ::to { it.destination() }
                                text { ::content { it.title } }
                            } in bar
                        }
                    }
                }
                renderGroup(appNav.actionsProperty)
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
            compact - button {
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
            compact - row {
                navGroupActions(appNav.actionsProperty)
            }
            ::exists { appNav.existsProperty.await() }
        } in bar in marginless
        navigatorView(navigator) in weight(1f) in marginless
        //Nav 3 - top and bottom (bottom/tabs)
        row {
            ::exists { appNav.existsProperty.await() && !SoftInputOpen.await() }
            navGroupTabs(appNav.navItemsProperty)
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
                navGroupActions(appNav.actionsProperty)
            }
            row {
                image {
                    val currentTheme = currentTheme
                    ::source {
                        appNav.currentUserProperty.await()?.profileImage ?: Icon.person.toImageSource(
                            currentTheme().foreground
                        )
                    }
                    description = "User icon"
                }
                text {
                    ::content { appNav.currentUserProperty.await()?.name ?: "No user" }
                } in gravity(
                    Align.Center,
                    Align.Center
                )
            } in withDefaultPadding in hasPopover {
                col {
                    navGroupColumn(appNav.userLinksProperty)
                } in card
            }

            ::exists { appNav.existsProperty.await() }
        } in bar in marginless
        row {
            col {
                navGroupColumn(appNav.navItemsProperty)
                ::exists { appNav.navItemsProperty.await().size > 1 && appNav.existsProperty.await() }
            } in withDefaultPadding
            separator {
                ::exists { appNav.navItemsProperty.await().size > 1 && appNav.existsProperty.await() }
            }
            navigatorView(navigator) in weight(1f) in marginless
        } in weight(1f)
    } in marginless
}
