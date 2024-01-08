package com.lightningkite.rock.views.direct

import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.canvas.DrawingContext2D
import kotlin.jvm.JvmInline

expect class NCanvas : NView

@JvmInline
value class Canvas(override val native: NCanvas) : RView<NCanvas>

@ViewDsl
expect fun ViewWriter.canvas(setup: Canvas.() -> Unit = {}): Unit
expect fun Canvas.redraw(action: DrawingContext2D.() -> Unit): Unit
expect val Canvas.width: Readable<Double>
expect val Canvas.height: Readable<Double>
expect fun Canvas.onPointerDown(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit
expect fun Canvas.onPointerMove(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit
expect fun Canvas.onPointerCancel(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit
expect fun Canvas.onPointerUp(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit