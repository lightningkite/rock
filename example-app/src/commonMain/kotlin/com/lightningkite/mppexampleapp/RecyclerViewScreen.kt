package com.lightningkite.mppexampleapp

import com.lightningkite.rock.*
import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.models.rem
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

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
            sizedBox(SizeConstraints(height = 4.rem)) - horizontalRecyclerView {
                children(items) {
                    important - stack { text { ::content { it.await().toString() } } }
                }
            }
            recyclerView {
                recyclerView = this
                children(items) {
                    col {
                        row {
                            expanding - text { ::content { "Item ${it.await()}" } }
                            button {
                                text {
                                    ::content { if (expanded.await() == it.await()) "Expanded" else "Expand" }
                                }
                                onClick {
                                    expanded.value = it.await()
                                }
                            }
                        } in themeFromLast { theme ->
                            if(it.await() == 50) theme.important() else theme
                        }
                        col {
                            ::exists { expanded.await() == it.await() }
                            text("More Content")
                            text("More Content")
                            text("More Content")
                            text("More Content")
                            text("More Content")
                        }
                    }
                }
            } in weight(1f)
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