package com.lightningkite.rock.views.canvas

import com.lightningkite.rock.models.*
import org.w3c.dom.CanvasFillRule
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.CanvasTextAlign

actual typealias DrawingContext2D = CanvasRenderingContext2D
//actual typealias TextAlign = CanvasTextAlign

actual fun DrawingContext2D.drawCircle(x: Double, y: Double, radius: Double, startAngle: Double, endAngle: Double, anticlockwise: Boolean) = arc(x, y, radius, startAngle, endAngle, anticlockwise)
actual fun DrawingContext2D.drawText(text: String, x: Double, y: Double, maxWidth: Double):Unit = fillText(text, x, y, maxWidth)
//actual fun DrawingContext2D.font(size: Dimension, value: FontAndStyle) {
//    font = "${if(value.bold) "bold " else ""}${if(value.italic) "italic " else ""}${size.value}/${value.lineSpacingMultiplier} \"${value.font.cssFontFamilyName}\""
//}
//expect fun DrawingContext2D.textAlign(alignment: TextAlign) =
actual fun DrawingContext2D.fill() = fill("nonzero".asDynamic().unsafeCast<CanvasFillRule>())
actual fun DrawingContext2D.fillEvenOdd() = fill("evenodd".asDynamic().unsafeCast<CanvasFillRule>())

actual var DrawingContext2D.strokePaint: Paint
    get() = when(val it = strokeStyle) {
        is String -> Color.fromHexString(it)
        else -> Color.black
    }
    set(value) {
        when(value) {
            is Color -> strokeStyle = value.toWeb()
            is LinearGradient -> TODO()
            is RadialGradient -> TODO()
        }
    }
actual var DrawingContext2D.fillPaint: Paint
    get() = when(val it = fillStyle) {
        is String -> Color.fromHexString(it)
        else -> Color.black
    }
    set(value) {
        when(value) {
            is Color -> fillStyle = value.toWeb()
            is LinearGradient -> TODO()
            is RadialGradient -> TODO()
        }
    }
actual val DrawingContext2D.width: Double get() = canvas.width.toDouble()
actual val DrawingContext2D.height: Double get() = canvas.height.toDouble()