package com.lightningkite.rock.views.direct

import com.lightningkite.rock.dom.HTMLElement
import com.lightningkite.rock.models.Align
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.get

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
    writer.forEachUpdating(items, render = render)
}

actual fun RecyclerView.scrollToIndex(
    index: Int,
    align: Align?,
    animate: Boolean
) {
    this.native.children.let {
        (0 until it.length).map { index -> it.get(index) }.filterIsInstance<org.w3c.dom.HTMLElement>().getOrNull(index)?.let {
            val d: dynamic = js("{}")
            d.behavior = if(animate) "smooth" else "instant"
            d.inline = when(align) {
                Align.Start -> "start"
                Align.Center -> "center"
                Align.End -> "end"
                else -> "nearest"
            }
            d.block = when(align) {
                Align.Start -> "start"
                Align.Center -> "center"
                Align.End -> "end"
                else -> "nearest"
            }
            it.scrollIntoView(d)
        } ?: console.warn("No item with index $index found")
    }
}