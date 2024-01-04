@file:OptIn(ExperimentalForeignApi::class)

package com.lightningkite.rock.views.direct

import com.lightningkite.rock.launch
import com.lightningkite.rock.models.Action
import com.lightningkite.rock.views.*
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.UIKit.*
import platform.objc.sel_registerName
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.views.canvas.DrawingContext2D
import com.lightningkite.rock.views.canvas.DrawingContext2DImpl

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
class CanvasView : UIView(CGRectZero.readValue()) {
    init {
        opaque = false
        contentMode = UIViewContentMode.UIViewContentModeRedraw
    }

    val onPointerDown = ArrayList<(id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit>()
    val onPointerMove = ArrayList<(id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit>()
    val onPointerCancel = ArrayList<(id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit>()
    val onPointerUp = ArrayList<(id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit>()

    private fun ArrayList<(id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit>.invokeAll(
        id: Int, x: Double, y: Double, width: Double, height: Double
    ) {
        forEach { it.invoke(id, x, y, width, height) }
    }

    val touchIds = HashMap<UITouch, Int>()
    var currentTouchId: Int = 0
    override fun touchesBegan(touches: Set<*>, withEvent: UIEvent?) {
        handle(touches as Set<UITouch>)
        super.touchesBegan(touches, withEvent)
    }

    override fun touchesMoved(touches: Set<*>, withEvent: UIEvent?) {
        handle(touches as Set<UITouch>)
        super.touchesMoved(touches, withEvent)
    }

    override fun touchesEnded(touches: Set<*>, withEvent: UIEvent?) {
        handle(touches as Set<UITouch>)
        super.touchesEnded(touches, withEvent)
    }

    override fun touchesCancelled(touches: Set<*>, withEvent: UIEvent?) {
        handle(touches as Set<UITouch>)
        super.touchesCancelled(touches, withEvent)
    }

    private fun handle(touches: Set<UITouch>) {
        for (touch in touches) {
            val loc = touch.locationInView(this)
            when (touch.phase) {
                UITouchPhase.UITouchPhaseBegan -> {
                    val id = currentTouchId
                    currentTouchId += 1
                    touchIds[touch] = id
                    onPointerDown.invokeAll(
                        id = id,
                        x = (loc.useContents { x }),  // * UIScreen.mainScreen.scale,
                        y = (loc.useContents { y }),  // * UIScreen.mainScreen.scale,
                        width = (frame.useContents { size.width }),  // * UIScreen.mainScreen.scale,
                        height = (frame.useContents { size.height }),  // * UIScreen.mainScreen.scale
                    )
                }

                UITouchPhase.UITouchPhaseMoved -> {
                    touchIds[touch]?.let { id ->
                        onPointerMove.invokeAll(
                            id = id,
                            x = (loc.useContents { x }),  // * UIScreen.mainScreen.scale,
                            y = (loc.useContents { y }),  // * UIScreen.mainScreen.scale,
                            width = (frame.useContents { size.width }),  // * UIScreen.mainScreen.scale,
                            height = (frame.useContents { size.height }),  // * UIScreen.mainScreen.scale
                        )
                    }
                }

                UITouchPhase.UITouchPhaseCancelled -> {
                    touchIds[touch]?.let { id ->
                        onPointerCancel.invokeAll(
                            id = id,
                            x = (loc.useContents { x }),  // * UIScreen.mainScreen.scale,
                            y = (loc.useContents { y }),  // * UIScreen.mainScreen.scale,
                            width = (frame.useContents { size.width }),  // * UIScreen.mainScreen.scale,
                            height = (frame.useContents { size.height }),  // * UIScreen.mainScreen.scale
                        )
                    }
                    touchIds.remove(touch)
                }

                UITouchPhase.UITouchPhaseEnded -> {
                    touchIds[touch]?.let { id ->
                        onPointerUp.invokeAll(
                            id = id,
                            x = (loc.useContents { x }),  // * UIScreen.mainScreen.scale,
                            y = (loc.useContents { y }),  // * UIScreen.mainScreen.scale,
                            width = (frame.useContents { size.width }),  // * UIScreen.mainScreen.scale,
                            height = (frame.useContents { size.height }),  // * UIScreen.mainScreen.scale
                        )
                    }
                    touchIds.remove(touch)
                }
                else -> {}
            }
        }
    }

    var draw: (DrawingContext2D)->Unit = {}
    override fun drawRect(rect: CValue<CGRect>) {
        super.drawRect(rect)
        with(DrawingContext2DImpl(UIGraphicsGetCurrentContext()!!, rect.useContents { this.size.width }, rect.useContents { this.size.height })) {
            draw(this)
        }
    }
}
