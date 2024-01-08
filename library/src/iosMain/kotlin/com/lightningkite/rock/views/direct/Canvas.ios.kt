@file:OptIn(ExperimentalForeignApi::class)

package com.lightningkite.rock.views.direct

import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.canvas.DrawingContext2D
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NCanvas = CanvasView

@ViewDsl
actual fun ViewWriter.canvas(setup: Canvas.() -> Unit): Unit = element(CanvasView()) {
    setup(Canvas(this))
}

actual fun Canvas.redraw(action: DrawingContext2D.() -> Unit): Unit {
    this.native.draw = action
    this.native.setNeedsDisplay()
}

actual val Canvas.width: Readable<Double> get() = Property(this.native.bounds.useContents { size.width })
actual val Canvas.height: Readable<Double> get() = Property(this.native.bounds.useContents { size.height })
actual fun Canvas.onPointerDown(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {
    native.onPointerDown.add(action)
}

actual fun Canvas.onPointerMove(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {
    native.onPointerMove.add(action)
}

actual fun Canvas.onPointerCancel(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {
    native.onPointerCancel.add(action)
}

actual fun Canvas.onPointerUp(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {
    native.onPointerUp.add(action)
}