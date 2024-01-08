package com.lightningkite.rock.views.direct

import android.view.SurfaceView
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.canvas.DrawingContext2D

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NCanvas = SurfaceView

actual fun Canvas.redraw(action: DrawingContext2D.() -> Unit): Unit {}
actual val Canvas.width: Readable<Double>
    get() {
        return object : Readable<Double> {
            override fun addListener(listener: () -> Unit): () -> Unit {
                return this@width.native.addLayoutChangeListener(listener)
            }

            override suspend fun awaitRaw(): Double {
                return this@width.native.width.toDouble()
            }
        }
    }
actual val Canvas.height: Readable<Double>
    get() {
        return object : Readable<Double> {
            override fun addListener(listener: () -> Unit): () -> Unit {
                return this@height.native.addLayoutChangeListener(listener)
            }

            override suspend fun awaitRaw(): Double {
                return this@height.native.height.toDouble()
            }
        }
    }

actual fun Canvas.onPointerDown(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {}
actual fun Canvas.onPointerMove(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {}
actual fun Canvas.onPointerCancel(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {}
actual fun Canvas.onPointerUp(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {}

@ViewDsl
actual fun ViewWriter.canvas(setup: Canvas.() -> Unit) {
    return viewElement(factory = ::SurfaceView, wrapper = ::Canvas, setup = setup)
}