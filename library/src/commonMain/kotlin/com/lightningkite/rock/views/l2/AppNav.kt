package com.lightningkite.rock.views.l2

import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.PlatformNavigator
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.navigation.Routes
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.delegate
import com.lightningkite.rock.reactive.invoke
import com.lightningkite.rock.reactive.reactiveScope
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
    val children: List<NavItem>
)

data class UserInfo(val name: String, val profileImage: ImageVector? = null)


interface AppNav {
    var appName: String
    var appIcon: Icon
    var appLogo: ImageSource
    var navItems: List<NavItem>
    var currentUser: ProfileInfo?
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
    }
}

val ViewContext.appNavFactory by viewContextAddon<Property<ViewContext.(AppNav.() -> Unit) -> Unit>>(Property(ViewContext::appNavBottomTabs))
fun ViewContext.appNav(routes: Routes, setup: AppNav.() -> Unit) {
    swapView {
        val navigator = PlatformNavigator(routes)
        this@appNav.navigator = navigator
        val alt = split()
        reactiveScope {
            swap {
                appNavFactory.current.invoke(alt, setup)
            }
        }
    } in marginless
}

fun ViewContext.appNavHamburger(setup: AppNav.() -> Unit) {
    val appNav = AppNav.ByProperty()
    col {
        row {
            h1 { ::content.invoke { appNav.appNameProperty.current } }
            setup(appNav)
            row {
                forEachUpdating(appNav.navItemsProperty) {
                    link {
                        ::to { it.current.destination }
                        text { ::content { it.current.title } }
                    }
                }
            } in card
        }
        //stacked nav
        row {
            col {
                col {
                    forEachUpdating(appNav.navItemsProperty) {
                        link {
                            ::to { it.current.destination }
                            text { ::content { it.current.title } }
                        }
                    }
                }
            } in card
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
            h1 { ::content.invoke { appNav.appNameProperty.current } }
            row {
                forEachUpdating(appNav.navItemsProperty) {
                    link {
                        ::to { it.current.destination }
                        text { ::content { it.current.title } }
                    }
                }
            }
        } in card in marginless
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
                    val currentTheme = themeStack.last()
                    ::source { Icon.arrowBack.toImageSource(currentTheme().foreground) }
                    description = "Go Back"
                    ::visible { navigator.canGoBack.current }
                }
                onClick { navigator.goBack() }
            }
            h2 { ::content.invoke { navigator.currentScreen.current.title.current } } in gravity(Align.Center, Align.Center) in weight(1f)
            row {
                forEachUpdating(appNav.actionsProperty) {
                    button {
                        image {
                            val currentTheme = themeStack.last()
                            ::source { it.current.icon.toImageSource(currentTheme().foreground) }
                            ::description { it.current.title }
                        }
                        onClick { it.once.onSelect() }
                    }
                }
            }
        } in bar in marginless
        navigatorView(navigator) in weight(1f)
        //Nav 3 - top and bottom (bottom/tabs)
        row {
            forEachUpdating(appNav.navItemsProperty) {
                link {
                    ::to { it.current.destination }
                    col {
                        image {
                            val currentTheme = themeStack.last()
                            ::source { it.current.icon.toImageSource(currentTheme().foreground) }
                        } in gravity(Align.Center, Align.Center)
                        subtext { ::content { it.current.title } } in gravity(Align.Center, Align.Center)
                    }
                } in weight(1f) in marginless in themeFromLast { existing ->
                    if (navigator.currentScreen.current == it.current.destination)
                        existing.bar().down()
                    else
                        existing.bar()
                } in marginless
            }
        } in marginless
    } in marginless
}

fun ViewContext.appNavTopAndLeft(setup: AppNav.() -> Unit) {
    val appNav = AppNav.ByProperty()
    col {
// Nav 4 left and top - add dropdown for user info
        row {
            setup(appNav)
            h1 { ::content.invoke { appNav.appNameProperty.current } }
            row {
                forEachUpdating(appNav.navItemsProperty) {
                    link {
                        ::to { it.current.destination }
                        text { ::content { it.current.title } }
                    }
                }
            }
            row {
                image {
                    source = appNav.currentUser?.currentUser?.profileImage ?: Icon.add.toImageSource(Color.gray)
                    description = "User icon"
                }
                h6 { ::content.invoke { appNav.currentUser?.currentUser?.name ?: "No user information" } }
                col {
                    forEachUpdating(appNav.navItemsProperty) {
                        link {
                            ::to { it.current.destination }
                            text { ::content { it.current.title } }
                        }
                    }
                }
            }
        } in card in marginless
        row {
            col {
                col {
                    forEachUpdating(appNav.navItemsProperty) {
                        link {
                            ::to { it.current.destination }
                            text { ::content { it.current.title } }
                        }
                    }
                }
            } in card in marginless
            navigatorView(navigator) in weight(1f)
        } in weight(1f)
    } in marginless
}
///TO DO
//appNav()
//functions
//search, jump to, screen name, theme derivation for bar - tie nav bar styling to theme, Rock UI toggle theme, remove back arrow on root screen?
//Nav 1 - stacked nav toggle animation
//Nav 2 - update link text
//Nav 3 - text align center
//Nav 4 - pop over