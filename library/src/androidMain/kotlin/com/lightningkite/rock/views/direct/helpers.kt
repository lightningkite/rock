package com.lightningkite.rock.views.direct

import android.content.Context
import com.lightningkite.rock.views.*
import java.util.WeakHashMap


fun <T: NView, W: RView<T>> ViewWriter.viewElement(
    factory: (Context) -> T,
    wrapper: (T) -> W,
    setup: W.() -> Unit,
) {
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
