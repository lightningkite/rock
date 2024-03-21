package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.ScreenTransition
import com.lightningkite.rock.views.*
import kotlin.jvm.JvmInline
import kotlin.contracts.*

expect class NSwapView : NView

@JvmInline
value class SwapView(override val native: NSwapView) : RView<NSwapView>

@ViewDsl
expect fun ViewWriter.swapViewActual(setup: SwapView.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.swapView(noinline setup: SwapView.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; swapViewActual(setup) }
@ViewDsl
expect fun ViewWriter.swapViewDialogActual(setup: SwapView.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.swapViewDialog(noinline setup: SwapView.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; swapViewDialogActual(setup) }
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
            try {
                swap(transition(next)) { views(next) }
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }
        alreadySwapping = false
    }
}