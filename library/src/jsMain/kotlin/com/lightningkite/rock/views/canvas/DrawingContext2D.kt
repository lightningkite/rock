package com.lightningkite.rock.views.canvas

import com.lightningkite.rock.models.Color
import com.lightningkite.rock.models.LinearGradient
import com.lightningkite.rock.models.Paint
import com.lightningkite.rock.models.RadialGradient
import org.w3c.dom.CanvasFillRule
import org.w3c.dom.CanvasRenderingContext2D

actual typealias DrawingContext2D = CanvasRenderingContext2D

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