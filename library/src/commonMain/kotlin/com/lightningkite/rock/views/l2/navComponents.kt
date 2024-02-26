package com.lightningkite.rock.views.l2

import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*


fun ViewWriter.navGroupColumn(elements: Readable<List<NavElement>>, setup: ContainingView.()->Unit = {}) {
    col {
        navGroupColumnInner(elements)
        setup()
    }
}
private fun ViewWriter.navGroupColumnInner(readable: Readable<List<NavElement>>) {
    forEach(readable) {
        when (it) {
            is NavAction -> button {
                text { ::content { it.title() } }
                onClick { it.onSelect() }
            }

            is NavExternal -> externalLink {
                ::to { it.to() }
                text { ::content { it.title() } }
            }

            is NavGroup -> {
                col {
                    h3 { ::content { it.title() } }
                    row {
                        space()
                        col {
                            navGroupColumnInner(shared { it.children() })
                        }
                    }
                }
            }

            is NavLink -> link {
                ::to { it.destination() }
                text { ::content { it.title() } }
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
            is NavAction -> button {
//                text { ::content { it.title() } }
                icon {
                    ::source { it.icon() }
                    ::description { it.title() }
                }
                onClick { it.onSelect() }
            }

            is NavExternal -> externalLink {
                ::to { it.to() }
//                text { ::content { it.title() } }
                icon {
                    ::source { it.icon() }
                    ::description { it.title() }
                }
            }

            is NavGroup -> {
                row {
                    navGroupActionsInner(shared { it.children() })
                }
            }

            is NavLink -> link {
                ::to { it.destination() }
//                text { ::content { it.title() } }
                icon {
                    ::source { it.icon() }
                    ::description { it.title() }
                }
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
            is NavAction -> button {
                text { ::content { it.title() } }
                onClick { it.onSelect() }
            }

            is NavExternal -> externalLink {
                ::to { it.to() }
                text { ::content { it.title() } }
            }

            is NavGroup -> button {
                text { ::content { it.title() } }
            } in hasPopover {
                card - navGroupColumn(shared { it.children() })
            }

            is NavLink -> link {
                ::to { it.destination() }
                text { ::content { it.title() } }
            }
        }
    }
}

fun ViewWriter.navGroupTabs(readable: Readable<List<NavElement>>, setup: ContainingView.()->Unit) {
    row {
        spacing = 0.px
        setup()
        fun ViewWriter.display(navElement: NavElement) {
            compact - col {
                icon {
                    ::source { navElement.icon() }
                } in gravity(Align.Center, Align.Center)
                subtext { ::content { navElement.title() } } in gravity(Align.Center, Align.Center)
            }
        }
        forEach(readable) {
            when (it) {
                is NavAction -> button {
                    display(it)
                    onClick { it.onSelect() }
                }

                is NavExternal -> externalLink {
                    ::to { it.to() }
                    display(it)
                }

                is NavGroup -> button {
                    display(it)
                    onClick { }  // TODO: select dialog
                }

                is NavLink -> {
                    link {
                        display(it)
                        ::to { it.destination() }
                    } in themeFromLast { existing ->
                        if (navigator.currentScreen.await()?.let { navigator.routes.render(it) }?.urlLikePath?.segments == navigator.routes.render(
                                it.destination()
                            )?.urlLikePath?.segments)
                            (existing.bar() ?: existing).down()
                        else
                            existing.bar() ?: existing
                    }
                    Unit
                }
            } in weight(1f) 
        }
    } 
}