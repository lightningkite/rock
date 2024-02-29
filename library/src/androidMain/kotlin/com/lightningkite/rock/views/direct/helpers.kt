package com.lightningkite.rock.views.direct

import android.content.Context
import android.view.View
import com.lightningkite.rock.views.*
import java.lang.ref.WeakReference
import java.util.WeakHashMap


inline fun <T: NView, W: RView<T>> ViewWriter.viewElement(
    factory: (Context) -> T,
    wrapper: (T) -> W,
    crossinline setup: W.() -> Unit,
) {
    val native = factory(context)
    val wrapped = wrapper(native)
    element(native) {
        setup(wrapped)
    }
}

val View__listeners = WeakHashMap<View, HashMap<Any?, WeakReference<MutableList<()->Unit>>>>()
inline fun <V: View, L> V.addListener(noinline to: V.(L)->Unit, wrapper: (()->Unit)->L, noinline action: ()->Unit): ()->Unit {
    val core = View__listeners.getOrPut(this) { HashMap() }
    val list = core[to]?.get()
    if(list != null) list.add(action)
    else {
        val newList = ArrayList<()->Unit>()
        newList.add(action)
        to(wrapper {
            for(item in newList.toList()) item()
        })
        core[to] = WeakReference(newList)
    }
    return {
        View__listeners.get(this)?.get(to)?.get()?.remove(action)
    }
}
