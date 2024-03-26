package com.lightningkite.mppexampleapp

import com.lightningkite.rock.*
import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.models.px
import com.lightningkite.rock.models.rem
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.l2.lazyExpanding

@Routable("recycler-view/horizontal")
object HorizontalRecyclerViewScreen : RockScreen {
    override val title: Readable<String>
        get() = super.title

    override fun ViewWriter.render() {
        var expanded = Property(-1)
        val items = Property((1..101).toList())
        var recyclerView: RecyclerView? = null
        col {
            row {
                for(align in Align.values()) {
                    expanding - button {
                        subtext("Jump ${align.name}")
                        onClick { recyclerView?.scrollToIndex(49, align, false) }
                    }
                }
            }
            row {
                for(align in Align.values()) {
                    expanding - button {
                        subtext("Scroll ${align.name}")
                        onClick { recyclerView?.scrollToIndex(49, align, true) }
                    }
                }
            }
            row {
                repeat(4) {
                    val cols = it + 1
                    expanding - button {
                        subtext("${cols} columns")
                        onClick { recyclerView?.columns = cols }
                    }
                }
            }
            horizontalRecyclerView {
                recyclerView = this
                spacing = 0.5.rem
                columns = 2
                this.scrollToIndex(10, Align.Start)
                children(items) {
                    themeFromLast { theme ->
                        if(it.await() == 50) theme.important() else if(it.await() % 7 == 0) theme.hover() else theme
                    } - col {
                        row {
                            expanding - centered - text { ::content { "Item ${it.await()}" } }
                            centered - button {
                                text {
                                    ::content { if (expanded.await() == it.await()) "Expanded" else "Expand" }
                                }
                                onClick {
                                    expanded.value = if(it.await() == expanded.value) -1 else it.await()
                                    native.scrollIntoView(null, Align.Start, true)
                                }
                            }
                        }
                        lazyExpanding(shared { expanded.await() == it.await() }) {
                            row {
                                text("More Content")
                            }
                        }
                    }
                }
            } in weight(1f)
            row {
                text {
                    ::content {
                        "Min: ${recyclerView!!.firstVisibleIndex.await()}, Max: ${recyclerView!!.lastVisibleIndex.await()}"
                    }
                }
            }
        }
    }
}