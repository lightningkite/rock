package com.lightningkite.rock.views.canvas

import com.lightningkite.rock.models.Paint

actual abstract class DrawingContext2D {
    //  abstract   fun stroke(path: Path2D)

//  abstract   fun clip(fillRule: CanvasFillRule)
//  abstract   fun clip(path: Path2D, fillRule: CanvasFillRule)
//    abstract fun resetClip()

//  abstract   fun isPointInPath(x: Double, y: Double, fillRule: CanvasFillRule): Boolean
//  abstract   fun isPointInPath(path: Path2D, x: Double, y: Double, fillRule: CanvasFillRule): Boolean
//    abstract fun isPointInStroke(x: Double, y: Double): Boolean
//  abstract   fun isPointInStroke(path: Path2D, x: Double, y: Double): Boolean

//  abstract   fun drawFocusIfNeeded(element: Element)
//  abstract   fun drawFocusIfNeeded(path: Path2D, element: Element)

//    abstract fun scrollPathIntoView()
//  abstract   fun scrollPathIntoView(path: Path2D)

//    abstract fun fillText(text: String, x: Double, y: Double, maxWidth: Double)
//    abstract fun strokeText(text: String, x: Double, y: Double, maxWidth: Double)

//  abstract   fun measureText(text: String): TextMetrics
//    abstract var font: String
//  abstract   var textAlign: CanvasTextAlign
//  abstract   var textBaseline: CanvasTextBaseline
//  abstract   var direction: CanvasDirection

//  abstract   fun drawImage(image: CanvasImageSource, dx: Double, dy: Double)
//  abstract   fun drawImage(image: CanvasImageSource, dx: Double, dy: Double, dw: Double, dh: Double)
//  abstract   fun drawImage(image: CanvasImageSource, sx: Double, sy: Double, sw: Double, sh: Double, dx: Double, dy: Double, dw: Double, dh: Double)
//  abstract   fun createImageData(sw: Double, sh: Double): ImageData
//  abstract   fun createImageData(imagedata: ImageData): ImageData
//  abstract   fun getImageData(sx: Double, sy: Double, sw: Double, sh: Double): ImageData
//  abstract   fun putImageData(imagedata: ImageData, dx: Double, dy: Double)
//  abstract   fun putImageData(imagedata: ImageData, dx: Double, dy: Double, dirtyX: Double, dirtyY: Double, dirtyWidth: Double, dirtyHeight: Double)

//  abstract   fun addHitRegion(options: HitRegionOptions)
//    abstract fun removeHitRegion(id: String)
//    abstract fun clearHitRegions()

    //    abstract fun arc(x: Double, y: Double, radius: Double, startAngle: Double, endAngle: Double, anticlockwise: Boolean)
//    abstract fun ellipse(x: Double, y: Double, radiusX: Double, radiusY: Double, rotation: Double, startAngle: Double, endAngle: Double, anticlockwise: Boolean)
    actual abstract fun save()
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