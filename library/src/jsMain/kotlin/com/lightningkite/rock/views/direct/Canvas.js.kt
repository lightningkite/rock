package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.canvas.DrawingContext2D
import com.lightningkite.rock.views.element
import org.w3c.dom.CanvasLineCap
import org.w3c.dom.CanvasLineJoin
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.ROUND
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.WheelEvent
import org.w3c.dom.pointerevents.PointerEvent

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NCanvas = HTMLCanvasElement

@ViewDsl
actual fun ViewWriter.canvas(setup: Canvas.() -> Unit): Unit = element<HTMLCanvasElement>("canvas") {
    val c = Canvas(this)
    setup(c)
    tabIndex = 1
    onkeydown = { ev: KeyboardEvent -> c.delegate?.onKeyDown(ev.code) }
    onkeyup = { ev: KeyboardEvent -> c.delegate?.onKeyUp(ev.code) }
    onwheel = { ev: WheelEvent -> c.delegate?.onWheel(ev.deltaX, ev.deltaY, ev.deltaZ) }
    onpointerdown = { event ->
        val b = getBoundingClientRect()
        c.delegate?.onPointerDown(event.pointerId, event.pageX - b.x, event.pageY - b.y, b.width, b.height)
    }
    onpointermove = { event ->
        val b = getBoundingClientRect()
        c.delegate?.onPointerMove(event.pointerId, event.pageX - b.x, event.pageY - b.y, b.width, b.height)
    }
    onpointerup = { event ->
        val b = getBoundingClientRect()
        c.delegate?.onPointerUp(event.pointerId, event.pageX - b.x, event.pageY - b.y, b.width, b.height)
    }
    onpointercancel = { event ->
        val b = getBoundingClientRect()
        c.delegate?.onPointerCancel(event.pointerId, event.pageX - b.x, event.pageY - b.y, b.width, b.height)
    }
    onpointerleave = { event ->
        val b = getBoundingClientRect()
        c.delegate?.onPointerCancel(event.pointerId, event.pageX - b.x, event.pageY - b.y, b.width, b.height)
    }
    ResizeObserver { _, _ ->
        if (width != scrollWidth || height != scrollHeight) {
            width = scrollWidth
            height = scrollHeight
        }
        c.delegate?.onResize(scrollWidth.toDouble(), scrollHeight.toDouble())
    }.observe(this)
}

actual var Canvas.delegate: CanvasDelegate?
    get() = this.native.asDynamic().__ROCK_delegate__ as? CanvasDelegate
    set(value) {
        this.native.asDynamic().__ROCK_delegate__ = value
        value?.let { value ->
            value.invalidate = {
                native.getContext("2d").apply {
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
    actual val space: KeyCode get() = "Space"
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