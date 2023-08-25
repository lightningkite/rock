package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

fun ViewContext.myView(counter: Readable<Int>) {
    val prop = Property(1)
    val lat = Property<Double?>(null)
    val lon = Property<Double?>(null)

    watchGeolocation { pos ->
        lat set pos.latitude
        lon set pos.longitude
    }

    column {
        row {
            simpleLabel {
                ::text { "Lat: ${lat.current}" }
            }
            column {
                simpleLabel {
                    text = "Hello world"
                }
                simpleLabel {
                    text = "Hello world 2"
                } in withBackground(Background(fill = Color(1f, 1f, 1f, 1f)))
                simpleLabel {
                    ::text { "Lon: ${lon.current}" }
                } in withBackground(Background(fill = Color(1f, 1f, 0f, 0f)))
            }
        } in withBackground(Background(
            fill = RadialGradient(

                stops = listOf(
                    GradientStop(ratio = 0.5f, color=Color(1f, 1f, 0f, 0f)),
                    GradientStop(ratio = 0.6f, color=Color(1f, 0f, 1f, 0f)),
                    GradientStop(ratio = 0.7f, color=Color(1f, 0f, 0f, 1f)),
                )
            )
        ))
    }
}

//@Path("path/{id}")
//fun ViewContext.myScreen(id: Int) {
//
//}
//
////in link(MyScreen(42).apply { x = 4 })
//
//@Path("path/{id}")
//class MyScreen(val id: Int) : Screen {
//    val x = queryParam("x", 1)
//
//    override fun ViewContext.invoke() {
//        val subscreen = Property<Screen>()
//        val prop = Property(1)
//
//    }
//}