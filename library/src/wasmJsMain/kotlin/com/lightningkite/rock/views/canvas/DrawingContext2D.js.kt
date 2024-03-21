package com.lightningkite.rock.views.canvas

import com.lightningkite.rock.models.*
import com.lightningkite.rock.toKString
import org.w3c.dom.*

actual typealias DrawingContext2D = CanvasRenderingContext2D
//actual typealias TextAlign = CanvasTextAlign

actual fun DrawingContext2D.appendArc(x: Double, y: Double, radius: Double, startAngle: Angle, endAngle: Angle, anticlockwise: Boolean) = arc(x, y, radius, startAngle.radians.toDouble(), endAngle.radians.toDouble(), anticlockwise)
actual fun DrawingContext2D.drawOutlinedText(text: String, x: Double, y: Double):Unit = strokeText(text, x, y)
actual fun DrawingContext2D.drawText(text: String, x: Double, y: Double):Unit = fillText(text, x, y)
actual fun DrawingContext2D.font(size: Double, value: FontAndStyle) {
    font = "${if(value.bold) "bold " else ""}${if(value.italic) "italic " else ""}${size}px ${value.font.cssFontFamilyName}"
}
actual fun DrawingContext2D.textAlign(alignment: TextAlign){
    textAlign = when(alignment) {
        TextAlign.start -> CanvasTextAlign.START
        TextAlign.end -> CanvasTextAlign.END
        TextAlign.left -> CanvasTextAlign.LEFT
        TextAlign.right -> CanvasTextAlign.RIGHT
        TextAlign.center -> CanvasTextAlign.CENTER
    }
}
actual fun DrawingContext2D.fill() = fill(CanvasFillRule.NONZERO)
actual fun DrawingContext2D.fillEvenOdd() = fill(CanvasFillRule.EVENODD)

actual var DrawingContext2D.strokePaint: Paint
    get() = when(val it = strokeStyle) {
        is JsString -> Color.fromHexString(it.toKString())
        else -> Color.black
    }
    set(value) {
        when(value) {
            is Color -> strokeStyle = value.toWeb().toJsString()
            is LinearGradient -> TODO()
            is RadialGradient -> TODO()
        }
    }
actual var DrawingContext2D.fillPaint: Paint
    get() = when(val it = fillStyle) {
        is JsString -> Color.fromHexString(it.toKString())
        else -> Color.black
    }
    set(value) {
        when(value) {
            is Color -> fillStyle = value.toWeb().toJsString()
            is LinearGradient -> TODO()
            is RadialGradient -> TODO()
        }
    }
actual val DrawingContext2D.width: Double get() = canvas.width.toDouble()
actual val DrawingContext2D.height: Double get() = canvas.height.toDouble()

actual fun DrawingContext2D.clear() {
    clearRect(0.0, 0.0, width, height)
}