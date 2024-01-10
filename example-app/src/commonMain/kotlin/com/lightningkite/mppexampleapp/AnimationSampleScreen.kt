package com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.canvas.*
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.clockMillis
import com.lightningkite.rock.reactive.*

@Routable("sample/animation")
object AnimationSampleScreen : RockScreen {
    data class Point(val x: Double, val y: Double)
    override fun ViewWriter.render() = stack {
        canvas {
            var x: Double = 0.0
            val line = Property(ArrayList<Point>())
            val pointersDown = Property(setOf<Int>())
            onPointerDown { id, x, y, width, height ->
                launch {
                    pointersDown modify { it + id }
                }
            }
            onPointerUp { id, x, y, width, height ->
                launch {
                    pointersDown modify { it - id }
                }
            }
            onPointerCancel { id, x, y, width, height ->
                launch {
                    pointersDown modify { it - id }
                }
            }
            onPointerMove { id, x, y, width, height ->
                launch {
                    if (id in pointersDown.await()) {
                        line.await().add(Point(x, y))
                        line set line.await()
                    }
                }
            }
            var last = clockMillis()
            reactiveScope {
                rerunOn(AnimationFrame)
                val now = clockMillis()
                val diff = now - last
                last = now
                val line = line.await()
                x += diff * 0.1
                redraw {
                    x %= width
                    fillPaint = Color.white
                    clearRect(0.0, 0.0, width, height)
                    strokePaint = Color.black
                    lineWidth = 5.0
                    beginPath()
                    moveTo(x, 0.0)
                    lineTo(x, height)
                    stroke()

                    beginPath()
                    moveTo(x, 0.0)
                    for(point in line) {
                        lineTo(point.x, point.y)
                    }
                    stroke()
                }
            }
        }
    }
}