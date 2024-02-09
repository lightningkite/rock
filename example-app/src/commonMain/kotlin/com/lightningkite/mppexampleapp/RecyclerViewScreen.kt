package com.lightningkite.mppexampleapp

import com.lightningkite.rock.*
import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.models.rem
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.l2.lazyExpanding

@Routable("recycler-view")
object RecyclerViewScreen : RockScreen {
    override val title: Readable<String>
        get() = super.title

    override fun ViewWriter.render() {
        var expanded = Property(-1)
        val items = Property((1..100).toList())
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
            sizedBox(SizeConstraints(height = 5.rem)) - horizontalRecyclerView {
                children(items) {
                    important - stack { centered - text { ::content { it.await().toString() } } }
                }
            }
            recyclerView {
                recyclerView = this
                children(items) {
                    themeFromLast { theme ->
                        if(it.await() == 50) theme.important() else if(it.await() % 7 == 0) theme.hover() else theme
                    } - col {
                        row {
                            expanding - centered - text { ::content { "Item ${it.await()}" } }
                            button {
                                text {
                                    ::content { if (expanded.await() == it.await()) "Expanded" else "Expand" }
                                }
                                onClick {
                                    expanded.value = if(it.await() == expanded.value) -1 else it.await()
                                    native.scrollIntoView(null, Align.Start, true)
                                }
                            }
                        }
                        text("le sigh")
                        lazyExpanding(shared { expanded.await() == it.await() }) {
                            col {
                                ::exists
                                text { ::content { "Content for ${it.await()} == ${expanded.await()}" } }
                                text("More Content")
                                text("More Content")
                                text("More Content")
                                text("More Content")
                                text("More Content")
                                text("More Content")
                                text("More Content")
                                text("More Content")
                                text("More Content")
                                text("More Content")
                                text("More Content")
                                text("More Content")
                                text("More Content")
                                text("More Content")
                                text("More Content")
                                text("More Content")
                                text("More Content")
                                text("More Content")
                                text("More Content")
                                text("More Content")
                                text("More Content")
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
//            horizontalRecyclerView {
//                children(items) {
//                    text { ::content { "Item ${it.await()}" } }
//                }
//            } in weight(1f)
//            gridRecyclerView {
//                columns = 4
//                children(items) {
//                    text { ::content { "Item ${it.await()}" } }
//                }
//            } in weight(1f)
        }
    }
}