package com.lightningkite.rock.views.l2

import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*


fun ViewWriter.navGroupColumn(elements: Readable<List<NavElement>>, onNavigate: suspend ()->Unit = {}, setup: ContainingView.()->Unit = {}) {
    col {
        navGroupColumnInner(elements, onNavigate)
        setup()
    }
}
private fun ViewWriter.navGroupColumnInner(readable: Readable<List<NavElement>>, onNavigate: suspend ()->Unit = {}) {
    forEach(readable) {
        fun ViewWriter.display(navElement: NavElement) {
            row {
                centered - navElementIconAndCountHorizontal(navElement)
                text { ::content { navElement.title() } } in gravity(Align.Center, Align.Center)
                space(1.0)
            }
        }
        when (it) {
            is NavAction -> button {
                display(it)
                onClick { it.onSelect() }
            }

            is NavExternal -> externalLink {
                ::to { it.to() }
                display(it)
                this.onNavigate(onNavigate)
            }

            is NavGroup -> {
                col {
                    spacing = 0.px
                    padded - row {
                        centered - navElementIconAndCountHorizontal(it)
                        centered - text { ::content { it.title() } }
                    }
                    row {
                        spacing = 0.px
                        space()
                        expanding - col {
                            spacing = 0.px
                            navGroupColumnInner(shared { it.children() }, onNavigate)
                        }
                    }
                }
            }

            is NavCustom -> {
                it.long(this)
            }

            is NavLink -> link {
                ::to { it.destination() }
                display(it)
                this.onNavigate(onNavigate)
            } in maybeThemeFromLast { existing ->
                if (navigator.currentScreen.await()
                        ?.let { navigator.routes.render(it) } == navigator.routes.render(it.destination())
                )
                    existing.selected()
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
                navElementIconAndCount(it)
                onClick { it.onSelect() }
            }

            is NavExternal -> externalLink {
                ::to { it.to() }
//                text { ::content { it.title() } }
                navElementIconAndCount(it)
            }

            is NavGroup -> {
                row {
                    navGroupActionsInner(shared { it.children() })
                }
            }

            is NavCustom -> {
                it.square(this)
            }

            is NavLink -> link {
                ::to { it.destination() }
//                text { ::content { it.title() } }
                navElementIconAndCount(it)
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

            is NavCustom -> {
                it.square(this)
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

fun ViewWriter.navElementIconAndCount(navElement: NavElement) {
    stack {
        icon {
            ::source { navElement.icon() }
        } in gravity(Align.Center, Align.Center)
        navElement.count?.let { count ->
            gravity(Align.End, Align.Start) - compact - critical - stack {
                exists = false
                ::exists { count() != null }
                space(0.01)
                centered - text {
                    ::content { count()?.takeIf { it > 0 }?.toString() ?: "" }
                    textSize = 0.75.rem
                }
            }
        }
    }
}

fun ViewWriter.navElementIconAndCountHorizontal(navElement: NavElement) {
    row {
        centered - icon {
            ::source { navElement.icon().copy(width = 1.5.rem, height = 1.5.rem) }
        }
        navElement.count?.let { count ->
            centered  - compact - critical - stack {
                exists = false
                ::exists { count() != null }
                space(0.01)
                centered - text {
                    ::content { count()?.takeIf { it > 0 }?.toString() ?: "" }
                }
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
                centered - navElementIconAndCount(navElement)
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

                is NavCustom -> {
                    it.tall(this)
                }

                is NavLink -> {
                    link {
                        display(it)
                        ::to { it.destination() }
                    } in themeFromLast { existing ->
                        if (navigator.currentScreen.await()?.let { navigator.routes.render(it) }?.urlLikePath?.segments == navigator.routes.render(
                                it.destination()
                            )?.urlLikePath?.segments)
                            (existing.bar() ?: existing).selected()
                        else
                            existing.bar() ?: existing
                    }
                    Unit
                }
            } in weight(1f) 
        }
    } 
}