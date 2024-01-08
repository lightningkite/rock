package com.lightningkite.rock.views.direct

import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import org.w3c.dom.HTMLDivElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRecyclerView = HTMLDivElement

@ViewDsl
actual fun ViewWriter.recyclerView(setup: RecyclerView.() -> Unit): Unit =
    themedElement<NRecyclerView>("div") {
        classList.add("recycler")
        this.asDynamic().__viewWriter = split()
        setup(RecyclerView(this))
    }

@ViewDsl
actual fun ViewWriter.horizontalRecyclerView(setup: RecyclerView.() -> Unit): Unit =
    themedElement<NRecyclerView>("div") {
        classList.add("recycler-horz")
        this.asDynamic().__viewWriter = split()
        setup(RecyclerView(this))
    }

@ViewDsl
actual fun ViewWriter.gridRecyclerView(setup: RecyclerView.() -> Unit): Unit =
    themedElement<NRecyclerView>("div") {
        classList.add("recycler-grid")
        this.asDynamic().__viewWriter = split()
        setup(RecyclerView(this))
    }

actual var RecyclerView.columns: Int
    get() = 1
    set(value) {
        TODO()
    }

actual fun <T> RecyclerView.children(
    items: Readable<List<T>>,
    render: ViewWriter.(value: Readable<T>) -> Unit
): Unit {
    val writer = this.native.asDynamic().__viewWriter as ViewWriter
    writer.forEachUpdating(items, render)
}