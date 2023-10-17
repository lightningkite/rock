package com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.AnimationFrame
import com.lightningkite.rock.reactive.reactiveScope
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.canvas.*
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.clockMillis

@Routable("sample/animation")
object AnimationSampleScreen : RockScreen {
    data class Point(val x: Double, val y: Double)
    override fun ViewContext.render() {
        canvas {
            var x: Double = 0.0
            val line = Property(ArrayList<Point>())
            val pointersDown = Property(setOf<Int>())
            onPointerDown { id, x, y, width, height ->
                pointersDown modify { it + id }
            }
            onPointerUp { id, x, y, width, height ->
                pointersDown modify { it - id }
            }
            onPointerCancel { id, x, y, width, height ->
                pointersDown modify { it - id }
            }
            onPointerMove { id, x, y, width, height ->
                if(id in pointersDown.once) {
                    line.once.add(Point(x, y))
                    line set line.once
                }
            }
            var last = clockMillis()
            reactiveScope {
                rerunOn(AnimationFrame)
                val now = clockMillis()
                val diff = now - last
                last = now
                redraw {
                    fillPaint = Color.white
                    clearRect(0.0, 0.0, width, height)
                    strokePaint = Color.black
                    lineWidth = 5.0
                    x += diff * 0.1
                    x %= width
                    beginPath()
                    moveTo(x, 0.0)
                    lineTo(x, height)
                    stroke()

                    beginPath()
                    moveTo(x, 0.0)
                    for(point in line.current) {
                        lineTo(point.x, point.y)
                    }
                    stroke()
                }
            }
        }
    }
}