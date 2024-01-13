package com.lightningkite.rock.views.direct

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.lightningkite.rock.models.Align
import com.lightningkite.rock.reactive.LateInitProperty
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.views.*
import androidx.recyclerview.widget.RecyclerView as AndroidRecyclerView


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NRecyclerView(context: Context) : AndroidRecyclerView(context) {
    lateinit var viewWriter: ViewWriter
}

actual fun <T> RecyclerView.children(items: Readable<List<T>>, render: ViewWriter.(value: Readable<T>) -> Unit): Unit {
    native.adapter = object : ObservableRVA<T>(this, { 0 }, { _, obs -> render(obs) }) {
        init {
            reactiveScope {
                val new = items.await().toList()
                lastPublished = new
                this.notifyDataSetChanged()
            }
        }
    }
}

@ViewDsl
actual fun ViewWriter.recyclerView(setup: RecyclerView.() -> Unit) {
    viewElement(factory = ::NRecyclerView, wrapper = ::RecyclerView) {
        native.viewWriter = newViews()
        native.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        setup()
    }
}

@ViewDsl
actual fun ViewWriter.horizontalRecyclerView(setup: RecyclerView.() -> Unit) {
    viewElement(factory = ::NRecyclerView, wrapper = ::RecyclerView) {
        native.viewWriter = newViews()
        native.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        setup()
    }
}

@ViewDsl
actual fun ViewWriter.gridRecyclerView(setup: RecyclerView.() -> Unit) {
    viewElement(factory = ::NRecyclerView, wrapper = ::RecyclerView) {
        native.viewWriter = newViews()
        native.layoutManager = GridLayoutManager(context, 3)
        setup()
    }
}

actual var RecyclerView.columns: Int
    get() = (native.layoutManager as? GridLayoutManager)?.spanCount ?: 0
    set(value) {
        (native.layoutManager as? GridLayoutManager)?.spanCount = value
    }


internal open class ObservableRVA<T>(
    val recyclerView: RecyclerView,
    val determineType: (T) -> Int,
    val makeView: ViewWriter.(Int, Readable<T>) -> Unit
) : AndroidRecyclerView.Adapter<AndroidRecyclerView.ViewHolder>() {
    var lastPublished: List<T> = listOf()
    val viewWriter = recyclerView.native.viewWriter

    override fun getItemViewType(position: Int): Int {
        return determineType(lastPublished[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AndroidRecyclerView.ViewHolder {
        val event = LateInitProperty<T>()
        viewWriter.makeView(viewType, event)
        val subview = viewWriter.rootCreated!!
        subview.layoutParams = AndroidRecyclerView.LayoutParams(
            if ((recyclerView.native.layoutManager as? LinearLayoutManager)?.orientation == LinearLayoutManager.VERTICAL)
                AndroidRecyclerView.LayoutParams.MATCH_PARENT else AndroidRecyclerView.LayoutParams.WRAP_CONTENT,
            if ((recyclerView.native.layoutManager as? LinearLayoutManager)?.orientation == LinearLayoutManager.HORIZONTAL)
                AndroidRecyclerView.LayoutParams.MATCH_PARENT else AndroidRecyclerView.LayoutParams.WRAP_CONTENT,
        )
        subview.tag = event
        recyclerView.native.calculationContext.onRemove { subview.shutdown() }
        return object : AndroidRecyclerView.ViewHolder(subview) {}
    }

    override fun getItemCount(): Int = lastPublished.size

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: AndroidRecyclerView.ViewHolder, position: Int) {
        (holder.itemView.tag as? LateInitProperty<T> ?: run {
            println("Failed to find property to update")
            null
        })?.value = (lastPublished[position])
    }
}

actual fun RecyclerView.scrollToIndex(
    index: Int,
    align: Align?,
    animate: Boolean
) {
    if (animate) {
        when (val lm = native.layoutManager ?: return) {
            is LinearLayoutManager -> if (align == null) lm.smoothScrollToPosition(
                native,
                AndroidRecyclerView.State(),
                index
            ) else lm.startSmoothScroll(AlignSmoothScroller(native.context, align).also { it.targetPosition = index })

            else -> lm.smoothScrollToPosition(native, AndroidRecyclerView.State(), index)
        }
    } else {
        when (val lm = native.layoutManager ?: return) {
            is LinearLayoutManager -> if (align == null) lm.scrollToPosition(index)
            else lm.scrollToPositionWithOffset(index, native.height / 2)

            else -> lm.scrollToPosition(index)
        }
    }
}


private class AlignSmoothScroller(context: Context, val align: Align?) : LinearSmoothScroller(context) {
    override fun calculateDtToFit(viewStart: Int, viewEnd: Int, boxStart: Int, boxEnd: Int, snapPreference: Int): Int {
        return when (align) {
            Align.Start -> boxStart - viewStart
            Align.Center -> boxStart + (boxEnd - boxStart) / 2 - (viewStart + (viewEnd - viewStart) / 2)
            Align.End -> boxStart + (boxEnd - boxStart) - (viewStart + (viewEnd - viewStart))
            else -> boxStart + (boxEnd - boxStart) / 2 - (viewStart + (viewEnd - viewStart) / 2)
        }
    }
}