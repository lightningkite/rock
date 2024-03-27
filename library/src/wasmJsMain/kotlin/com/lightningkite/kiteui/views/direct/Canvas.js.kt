package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.NView2
import com.lightningkite.kiteui.views.ViewWriter
import com.lightningkite.kiteui.views.canvas.DrawingContext2D
import com.lightningkite.kiteui.views.element
import org.w3c.dom.CanvasLineCap
import org.w3c.dom.CanvasLineJoin
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.ROUND
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.WheelEvent

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NCanvas(override val js: HTMLCanvasElement): NView2<HTMLCanvasElement>() {
    var delegate: CanvasDelegate? = null
}

@ViewDsl
actual inline fun ViewWriter.canvasActual(crossinline setup: Canvas.() -> Unit): Unit = element("canvas", ::NCanvas) {
    val c = Canvas(this)
    setup(c)
    js.tabIndex = 1
    js.onkeydown = { event: KeyboardEvent ->
        if(c.delegate?.onKeyDown(event.code) == true)
            event.preventDefault()
    }
    js.onkeyup = { event: KeyboardEvent ->
        if(c.delegate?.onKeyUp(event.code) == true)
            event.preventDefault()
    }
    js.onwheel = { event: WheelEvent ->
        if(c.delegate?.onWheel(event.deltaX, event.deltaY, event.deltaZ) == true)
            event.preventDefault()
    }
    js.onpointerdown = { event ->
        val b = js.getBoundingClientRect()
        if(c.delegate?.onPointerDown(event.pointerId, event.pageX - b.x, event.pageY - b.y, b.width, b.height) == true)
            event.preventDefault()
    }
    js.onpointermove = { event ->
        val b = js.getBoundingClientRect()
        if(c.delegate?.onPointerMove(event.pointerId, event.pageX - b.x, event.pageY - b.y, b.width, b.height) == true)
            event.preventDefault()
    }
    js.onpointerup = { event ->
        val b = js.getBoundingClientRect()
        if(c.delegate?.onPointerUp(event.pointerId, event.pageX - b.x, event.pageY - b.y, b.width, b.height) == true)
            event.preventDefault()
    }
    js.onpointercancel = { event ->
        val b = js.getBoundingClientRect()
        if(c.delegate?.onPointerCancel(event.pointerId, event.pageX - b.x, event.pageY - b.y, b.width, b.height) == true)
            event.preventDefault()
    }
    js.onpointerleave = { event ->
        val b = js.getBoundingClientRect()
        if(c.delegate?.onPointerCancel(event.pointerId, event.pageX - b.x, event.pageY - b.y, b.width, b.height) == true)
            event.preventDefault()
    }
    ResizeObserver { _, _ ->
        if (js.width != js.scrollWidth || js.height != js.scrollHeight) {
            js.width = js.scrollWidth
            js.height = js.scrollHeight
        }
        c.delegate?.onResize(js.scrollWidth.toDouble(), js.scrollHeight.toDouble())
        c.delegate?.invalidate?.invoke()
    }.observe(js)
}

actual var Canvas.delegate: CanvasDelegate?
    get() = native.delegate
    set(value) {
        native.delegate = value
        value?.let { value ->
            value.invalidate = {
                native.js.getContext("2d").apply {
                    this as DrawingContext2D
                    this.lineCap = CanvasLineCap.ROUND
                    this.lineJoin = CanvasLineJoin.ROUND
                    value.draw(this)
                }
            }
            value.invalidate()
        }
    }

actual typealias KeyCode = String
actual object KeyCodes {
    actual val left: KeyCode get() = "ArrowLeft"
    actual val right: KeyCode get() = "ArrowRight"
    actual val up: KeyCode get() = "ArrowUp"
    actual val down: KeyCode get() = "ArrowDown"
    actual fun letter(char: Char): KeyCode = "Key" + char.uppercase()
    actual fun num(digit: Int): KeyCode = "Digit$digit"
    actual fun numpad(digit: Int): KeyCode = "Numpad$digit"
    actual val space: KeyCode get() = " "
    actual val enter: KeyCode get() = "Enter"
    actual val tab: KeyCode get() = "Tab"
    actual val escape: KeyCode get() = "Escape"
    actual val leftCtrl: KeyCode get() = "ControlLeft"
    actual val rightCtrl: KeyCode get() = "ControlRight"
    actual val leftShift: KeyCode get() = "ShiftLeft"
    actual val rightShift: KeyCode get() = "ShiftRight"
    actual val leftAlt: KeyCode get() = "AltLeft"
    actual val rightAlt: KeyCode get() = "AltRight"
    actual val equals: KeyCode get() = "Equal"
    actual val dash: KeyCode get() = "Minus"
    actual val backslash: KeyCode get() = "Backslash"
    actual val leftBrace: KeyCode get() = "BracketLeft"
    actual val rightBrace: KeyCode get() = "BracketRight"
    actual val semicolon: KeyCode get() = "Semicolon"
    actual val comma: KeyCode get() = "Comma"
    actual val period: KeyCode get() = "Period"
}