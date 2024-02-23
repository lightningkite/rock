package com.lightningkite.rock.views.l2

import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*


/*

appNav(
    nav = {
        navLink {
            ::exists {}
        }
    }
)

 */

interface NavLink {
    val calculationContext: CalculationContext

    var content: String
    var icon: Icon
    var to: RockScreen
    var count: Int?
}

class NavLinkBottomTab(viewWriter: ViewWriter): NavLink {
    val iconProp = Property<Icon>(Icon.menu)
    val label: TextView
    val link: Link
    val countLabel: TextView
    val countHolder: ContainingView
    init {
        with(viewWriter) {
            marginless - expanding - link {
                link = this
                compact - col {
                    centered - stack {
                        icon({ iconProp.await() }, "")
                        gravity(Align.End, Align.Start) - compact - critical - stack {
                            exists = false
                            countHolder = this
                            text {
                                countLabel = this
                                textSize = 0.5.rem
                            }
                        }
                    }
                    centered - subtext { label = this }
                }
            }
        }
    }
    override val calculationContext: CalculationContext get() = label.calculationContext
    override var content by label::content
    override var icon: Icon by iconProp
    override var to: RockScreen by link::to
    override var count: Int?
        get() = countLabel.content.toIntOrNull()
        set(value) {
            countLabel.content = value?.takeIf { it > 0 }?.toString() ?: ""
            countHolder.exists = value != null
        }
}

var ViewWriter.navLinkConstructor: (ViewWriter)->NavLink by viewWriterAddon(::NavLinkBottomTab)
fun ViewWriter.navLink(setup: NavLink.()->Unit) { navLinkConstructor(this).apply { CalculationContextStack.useIn(calculationContext) { setup() } } }


fun ViewWriter.navGroupColumn(elements: Readable<List<NavElement>>, setup: ContainingView.()->Unit = {}) {
    col {
        navGroupColumnInner(elements)
        setup()
    }
}
private fun ViewWriter.navGroupColumnInner(readable: Readable<List<NavElement>>) {
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
                            navGroupColumnInner(shared { it.children() })
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

fun ViewWriter.navGroupActions(elements: Readable<List<NavElement>>, setup: ContainingView.()->Unit = {}) {
     row {
        navGroupActionsInner(elements)
        setup()
    }
}
private fun ViewWriter.navGroupActionsInner(readable: Readable<List<NavElement>>) {
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
                    navGroupActionsInner(shared { it.children() })
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

fun ViewWriter.navGroupTop(readable: Readable<List<NavElement>>, setup: ContainingView.()->Unit = {}) {
    row {
        navGroupTopInner(readable)
        setup()
    }
}
private fun ViewWriter.navGroupTopInner(readable: Readable<List<NavElement>>) {
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

            is NavGroup -> button {
                text { ::content { it.title } }
            } in hasPopover {
                card - navGroupColumn(shared { it.children() })
            }

            is NavItem -> link {
                ::to { it.destination() }
                text { ::content { it.title } }
            }
        }
    }
}

fun ViewWriter.navGroupTabs(readable: Readable<List<NavElement>>, setup: ContainingView.()->Unit) {
    row {
        setup()
        ::exists { !SoftInputOpen.await() }
        fun display(navElement: NavElement) {
            compact - col {
                image {
                    val currentTheme = currentTheme
                    ::source { navElement.icon.toImageSource(currentTheme().foreground) }
                } in gravity(Align.Center, Align.Center)
                subtext { ::content { navElement.title } } in gravity(Align.Center, Align.Center)
            }
        }
        forEach(readable) {
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
    } in marginless
}