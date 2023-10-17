package com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.contains
import com.lightningkite.rock.fetch
import com.lightningkite.rock.launch
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.AnimationFrame
import com.lightningkite.rock.reactive.bind
import com.lightningkite.rock.reactive.reactiveScope
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.canvas.*
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.clockMillis
import kotlin.coroutines.intrinsics.createCoroutineUnintercepted

@Routable("sample/canvas")
object CanvasSampleScreen : RockScreen {
    data class Point(val x: Double, val y: Double)

    override fun ViewContext.render() {
        canvas {
            var x: Double = 0.0
            val lines = Property(ArrayList<ArrayList<Point>>())
            val line = Property<ArrayList<Point>?>(null)
            val pointersDown = Property(setOf<Int>())
            onPointerDown { id, x, y, width, height ->
                pointersDown modify { it + id }
                val new = ArrayList<Point>()
                line set new
                lines modify { it.add(new); it }
            }
            onPointerUp { id, x, y, width, height ->
                pointersDown modify { it - id }
                line set null
            }
            onPointerCancel { id, x, y, width, height ->
                pointersDown modify { it - id }
                line set null
            }
            onPointerMove { id, x, y, width, height ->
                if (id in pointersDown.once) {
                    line.once?.add(Point(x, y))
                    line set line.once
                }
            }
            var last = clockMillis()
            reactiveScope {
                rerunOn(width)
                rerunOn(height)
                redraw {
                    fillPaint = Color.white
                    clearRect(0.0, 0.0, width, height)
                    strokePaint = Color.black
                    lineWidth = 5.0

                    for (line in lines.current) {
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
