package com.lightningkite.rock.views.canvas

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceView
import android.view.View
import com.lightningkite.rock.models.Paint


abstract class DrawingView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


}
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual abstract class DrawingContext2D {

    actual fun save() {}
    actual abstract fun restore()
    actual abstract fun scale(x: Double, y: Double)
    actual abstract fun rotate(angle: Double)
    actual abstract fun translate(x: Double, y: Double)
    actual abstract fun transform(
        a: Double,
        b: Double,
        c: Double,
        d: Double,
        e: Double,
        f: Double,
    )

    actual abstract fun setTransform(
        a: Double,
        b: Double,
        c: Double,
        d: Double,
        e: Double,
        f: Double,
    )

    actual abstract fun resetTransform()
    actual abstract var globalAlpha: Double
    actual abstract var globalCompositeOperation: String
    actual abstract var imageSmoothingEnabled: Boolean
    actual abstract var shadowOffsetX: Double
    actual abstract var shadowOffsetY: Double
    actual abstract var shadowBlur: Double
    actual abstract var shadowColor: String
    actual abstract var filter: String
    actual abstract fun clearRect(x: Double, y: Double, w: Double, h: Double)
    actual abstract fun fillRect(x: Double, y: Double, w: Double, h: Double)
    actual abstract fun strokeRect(x: Double, y: Double, w: Double, h: Double)
    actual abstract fun beginPath()
    actual abstract fun stroke()
    actual abstract var lineWidth: Double
    actual abstract var miterLimit: Double
    actual abstract var lineDashOffset: Double
    actual abstract fun setLineDash(segments: Array<Double>)
    actual abstract fun getLineDash(): Array<Double>
    actual abstract fun closePath()
    actual abstract fun moveTo(x: Double, y: Double)
    actual abstract fun lineTo(x: Double, y: Double)
    actual abstract fun quadraticCurveTo(cpx: Double, cpy: Double, x: Double, y: Double)
    actual abstract fun bezierCurveTo(
        cp1x: Double,
        cp1y: Double,
        cp2x: Double,
        cp2y: Double,
        x: Double,
        y: Double,
    )

    actual abstract fun arcTo(
        x1: Double,
        y1: Double,
        x2: Double,
        y2: Double,
        radius: Double,
    )

    actual abstract fun arcTo(
        x1: Double,
        y1: Double,
        x2: Double,
        y2: Double,
        radiusX: Double,
        radiusY: Double,
        rotation: Double,
    )

    actual abstract fun rect(x: Double, y: Double, w: Double, h: Double)
}

actual fun DrawingContext2D.fill() { TODO() }
actual fun DrawingContext2D.fillEvenOdd()  { TODO() }
actual var DrawingContext2D.strokePaint: Paint
    get() = TODO("implemenet")
    set(value) = TODO()

actual var DrawingContext2D.fillPaint: Paint
    get() = TODO("implemenet")
    set(value) = TODO()
actual val DrawingContext2D.width: Double
    get() = TODO("implemenet")
actual val DrawingContext2D.height: Double
    get() = TODO("implemenet")