package com.lightningkite.rock.views.direct

import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.canvas.DrawingContext2D
import com.lightningkite.rock.views.element
import org.w3c.dom.CanvasLineCap
import org.w3c.dom.CanvasLineJoin
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.ROUND

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NCanvas = HTMLCanvasElement

@ViewDsl
actual fun ViewWriter.canvas(setup: Canvas.() -> Unit): Unit = element<HTMLCanvasElement>("canvas") {
    setup(Canvas(this))
}

actual fun Canvas.redraw(action: DrawingContext2D.() -> Unit): Unit {
    if (native.width != native.scrollWidth || native.height != native.scrollHeight) {
        native.width = native.scrollWidth
        native.height = native.scrollHeight
    }
    native.getContext("2d").apply {
        this as DrawingContext2D
        this.lineCap = CanvasLineCap.ROUND
        this.lineJoin = CanvasLineJoin.ROUND
        action(this)
    }
}

actual val Canvas.width: Readable<Double> get() = SizeReader(native, "scrollWidth")
actual val Canvas.height: Readable<Double> get() = SizeReader(native, "scrollHeight")
actual fun Canvas.onPointerDown(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {
    native.addEventListener("pointerdown", pointerListenerHandler(action))
}

actual fun Canvas.onPointerMove(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {
    native.addEventListener("pointermove", pointerListenerHandler(action))
}

actual fun Canvas.onPointerCancel(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {
    val l = pointerListenerHandler(action)
    native.addEventListener("pointercancel", l)
    native.addEventListener("pointerleave", l)
}

actual fun Canvas.onPointerUp(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {
    native.addEventListener("pointerup", pointerListenerHandler(action))
}