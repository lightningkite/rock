package com.lightningkite.rock.views.direct

import android.content.Context
import android.view.View
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import java.util.UUID
import java.util.WeakHashMap
import kotlin.properties.ReadWriteProperty


@ViewDsl
inline fun <T: NView, W: RView<T>> ViewWriter.element(factory: (Context)->T, wrapper: (T)->W, crossinline setup: W.() -> Unit) {
    val native = factory(currentView.context)
    val wrapped = wrapper(native)
    element(native) {
        setup(wrapped)
    }
}

class ViewListeners<T: NView> {
    private val map = WeakHashMap<T, MutableList<()->Unit>>()
    fun get(key: T): (List<() -> Unit>)? = map[key]!!

    fun addListener(key: T, listener: () -> Unit) {
        map.putIfAbsent(key, mutableListOf())
        map[key]?.add(listener)
    }
    fun removeListener(key: T, listener: () -> Unit) {
        map[key]?.remove(listener)
    }

    fun removeKey(key: T) {
        map.remove(key)
    }
}
