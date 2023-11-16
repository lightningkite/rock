package com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.canvas.*
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.clockMillis
import com.lightningkite.rock.reactive.*

@Routable("sample/canvas")
object CanvasSampleScreen : RockScreen {
    data class Point(val x: Double, val y: Double)

    override fun ViewWriter.render() {
        canvas {
            var x: Double = 0.0
            val lines = Property(ArrayList<ArrayList<Point>>())
            val line = Property<ArrayList<Point>?>(null)
            val pointersDown = Property(setOf<Int>())
            onPointerDown { id, x, y, width, height ->
                launch {
                    pointersDown modify { it + id }
                    val new = ArrayList<Point>()
                    line set new
                    lines modify { it.add(new); it }
                }
            }
            onPointerUp { id, x, y, width, height ->
                launch {
                    pointersDown modify { it - id }
                    line set null
                }
            }
            onPointerCancel { id, x, y, width, height ->
                launch {
                    pointersDown modify { it - id }
                    line set null
                }
            }
            onPointerMove { id, x, y, width, height ->
                launch {
                    if (id in pointersDown.await()) {
                        line.await()?.add(Point(x, y))
                        line set line.await()
                    }
                }
            }
            var last = clockMillis()
            reactiveScope {
                rerunOn(width)
                rerunOn(height)
                val lines = lines.await()
                redraw {
                    fillPaint = Color.white
                    clearRect(0.0, 0.0, width, height)
                    strokePaint = Color.black
                    lineWidth = 5.0

                    for (line in lines) {
                        beginPath()
                        moveTo(line.firstOrNull()?.x ?: 0.0, line.firstOrNull()?.y ?: 0.0)
                        for (point in line) {
                            lineTo(point.x, point.y)
                        }
                        stroke()
                    }
                }
            }
        }
    }
//    fun Canvas.onPointerHold(action: suspend (get: suspend ()->Point)->Unit) {
//        action.createCoroutineUnintercepted(receiver = iterator, completion = )
//        onPointerDown {
//
//        }
//    }
}
