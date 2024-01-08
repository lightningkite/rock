package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.ScreenTransition
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline

expect class NSwapView : NView

@JvmInline
value class SwapView(override val native: NSwapView) : RView<NSwapView>

@ViewDsl
expect fun ViewWriter.swapView(setup: SwapView.() -> Unit = {}): Unit
@ViewDsl
expect fun ViewWriter.swapViewDialog(setup: SwapView.() -> Unit = {}): Unit
expect fun SwapView.swap(transition: ScreenTransition = ScreenTransition.Fade, createNewView: ()->Unit): Unit