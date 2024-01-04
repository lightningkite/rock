package com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp

import com.lightningkite.rock.*
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
        col {
            col {
                text { ::content { "Item 0" } }
                button {
                    text {
                        ::content { if(expanded.await() == 0) "Expanded" else "Expand" }
                    }
                    onClick {
                        expanded.value = 0
                    }
                }
                text {
                    content = "More content"
                    ::exists { expanded.await() == 0 }
                }
            }
            recyclerView {
                children(items) {
                    col {
                        text { ::content { "Item ${it.await()}" } }
                        button {
                            text {
                                ::content { if(expanded.await() == it.await()) "Expanded" else "Expand" }
                            }
                            onClick {
                                expanded.value = it.await()
                            }
                        }
                        text {
                            content = "More content"
                            ::exists { expanded.await() == it.await() }
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