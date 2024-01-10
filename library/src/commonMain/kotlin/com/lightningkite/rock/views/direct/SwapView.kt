package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.ScreenTransition
import com.lightningkite.rock.views.*
import kotlin.jvm.JvmInline

expect class NSwapView : NView

@JvmInline
value class SwapView(override val native: NSwapView) : RView<NSwapView>

@ViewDsl
expect fun ViewWriter.swapView(setup: SwapView.() -> Unit = {}): Unit
@ViewDsl
expect fun ViewWriter.swapViewDialog(setup: SwapView.() -> Unit = {}): Unit
expect fun SwapView.swap(transition: ScreenTransition = ScreenTransition.Fade, createNewView: ViewWriter.()->Unit): Unit

inline fun <T> SwapView.swapping(
    crossinline transition: (T) -> ScreenTransition = { ScreenTransition.Fade },
    crossinline current: suspend () -> T,
    crossinline views: ViewWriter.(T) -> Unit
): Unit {
    val queue = ArrayList<T>()
    var alreadySwapping = false
    reactiveScope {
        val c = current()
        queue.add(c)
        if(alreadySwapping) {
            return@reactiveScope
        }
        alreadySwapping = true
        while(queue.isNotEmpty()) {
            val next = queue.removeAt(0)
            swap(transition(next)) { views(next) }
        }
        alreadySwapping = false
    }
}