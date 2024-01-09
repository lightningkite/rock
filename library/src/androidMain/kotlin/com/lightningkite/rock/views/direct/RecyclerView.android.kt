package com.lightningkite.rock.views.direct

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.lightningkite.rock.reactive.LateInitProperty
import com.lightningkite.rock.reactive.Property
import androidx.recyclerview.widget.RecyclerView as AndroidRecyclerView
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.reactiveScope

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRecyclerView = AndroidRecyclerView

actual fun <T> RecyclerView.children(items: Readable<List<T>>, render: ViewWriter.(value: Readable<T>) -> Unit): Unit {
    native.adapter = object : ObservableRVA<T>({ 0 }, { _, obs -> render(obs) }) {
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
    viewElement(factory = ::AndroidRecyclerView, wrapper = ::RecyclerView) {
        native.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        setup()
    }
}

@ViewDsl
actual fun ViewWriter.horizontalRecyclerView(setup: RecyclerView.() -> Unit) {
    viewElement(factory = ::AndroidRecyclerView, wrapper = ::RecyclerView) {
        native.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        setup()
    }
}

@ViewDsl
actual fun ViewWriter.gridRecyclerView(setup: RecyclerView.() -> Unit) {
    viewElement(factory = ::AndroidRecyclerView, wrapper = ::RecyclerView) {
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
    val determineType: (T)->Int,
    val makeView: (Int, Readable<T>) -> View
): AndroidRecyclerView.Adapter<AndroidRecyclerView.ViewHolder>() {
    var lastPublished: List<T> = listOf()

    override fun getItemViewType(position: Int): Int {
        return determineType(lastPublished[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AndroidRecyclerView.ViewHolder {
        val event = LateInitProperty<T>()
        val subview = makeView(viewType, event)
//        subview.setRemovedCondition(removeCondition)
        subview.tag = event
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