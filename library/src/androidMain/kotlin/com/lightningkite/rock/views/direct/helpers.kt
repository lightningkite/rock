package com.lightningkite.rock.views.direct

import android.content.Context
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter


@ViewDsl
inline fun <T: NView, W: RView<T>> ViewWriter.element(factory: (Context)->T, wrapper: (T)->W, crossinline setup: W.() -> Unit) {
    val native = factory(currentView.context)
    val wrapped = wrapper(native)
    element(native) {
        setup(wrapped)
    }
}