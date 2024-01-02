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
        val items = Property((1..100).toList())
        col {
            recyclerView {
                children(items) {
                    text { ::content { "Item ${it.await().also { println("Text render $it") }}" } }
                }
            } in weight(1f)
            horizontalRecyclerView {
                children(items) {
                    text { ::content { "Item ${it.await().also { println("Text render $it") }}" } }
                }
            } in weight(1f)
            gridRecyclerView {
                columns = 4
                children(items) {
                    text { ::content { "Item ${it.await().also { println("Text render $it") }}" } }
                }
            } in weight(1f)
        }
    }
}