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
        val actionsProperty = Property<List<NavElement>>(listOf())
        override var actions: List<NavElement> by actionsProperty
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
    appBase(routes) {
        swapView {
            swapping(
                current = { appNavFactory.await() },
                views = { it(this, setup) }
            )
        }
    }
}

private val ViewWriter.compactBar get() = themeFromLast { (it.bar() ?: it).let { it.copy(spacing = it.spacing / 2) } }

fun ViewWriter.appNavHamburger(setup: AppNav.() -> Unit) {
    val appNav = AppNav.ByProperty()
    val showMenu = Property(false)
    col {
        spacing = 0.px
        compactBar - row {
            setup(appNav)
            toggleButton {
                checked bind showMenu
                image {
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
            navGroupActions(appNav.actionsProperty)
            ::exists { appNav.existsProperty.await() }
        }
        expanding - stack {
            spacing = 0.px
            navigatorView(navigator)
            row {
                spacing = 0.px
                bar
                onlyWhen(false) { showMenu.await() && appNav.existsProperty.await() }
                scrolls - navGroupColumn(appNav.navItemsProperty, { showMenu set false }) {
                    spacing = 0.px
                }
                weight(2f) - space {
                    ignoreInteraction = true
                }
            }
        }
    }
}


fun ViewWriter.appNavTop(setup: AppNav.() -> Unit) {
    val appNav = AppNav.ByProperty()
    // Nav 2 top, horizontal
    col {
        spacing = 0.px
        compactBar - row {
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
            space()
            expanding - centered - navGroupTop(appNav.navItemsProperty)
            space()
            centered - navGroupActions(appNav.actionsProperty)
            ::exists { appNav.existsProperty.await() }
        }
        navigatorView(navigator) in weight(1f)
    }
}

fun ViewWriter.appNavBottomTabs(setup: AppNav.() -> Unit) {
    val appNav = AppNav.ByProperty()
    col {
        spacing = 0.px
// Nav 3 top and bottom (top)
        compactBar - row {
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
            navGroupActions(appNav.actionsProperty)
            ::exists { appNav.existsProperty.await() }
        }
        navigatorView(navigator) in weight(1f)
        //Nav 3 - top and bottom (bottom/tabs)
        navGroupTabs(appNav.navItemsProperty) {
            ::exists { appNav.existsProperty.await() && !SoftInputOpen.await() }
        }
    }
}

fun ViewWriter.appNavTopAndLeft(setup: AppNav.() -> Unit) {
    val appNav = AppNav.ByProperty()
    col {
        spacing = 0.px
// Nav 4 left and top - add dropdown for user info
        compactBar - row {
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
            navGroupActions(appNav.actionsProperty)

            ::exists { appNav.existsProperty.await() }
        }
        row {
            spacing = 0.px
            nav - scrolls - navGroupColumn(appNav.navItemsProperty) {
                spacing = 0.px
                ::exists { appNav.navItemsProperty.await().size > 1 && appNav.existsProperty.await() }
            }
            navigatorView(navigator) in weight(1f)
        } in weight(1f)
    }
}
