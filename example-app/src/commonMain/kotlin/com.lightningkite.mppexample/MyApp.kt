package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

fun ViewContext.myView(counter: Readable<Int>) {
    val prop = Property(1)
    val lat = Property<Double?>(null)
    geolocate { latitude, longitude -> lat set latitude }
    column {
        simpleLabel {
            text = "Hi!  Welcome! ${counter.once}"
        }

        simpleLabel {
            ::text { "Lat: ${lat.current}" }
        }

        simpleLabel {
            ::text {
                "Counter value: ${counter.current}"
            }
        } in padding() in padding()

        padding()
        simpleLabel {
            ::text { "Counter value plus one: ${counter.current + prop.current}" }
        }

        simpleLabel {
            reactiveScope { text = "Counter value plus one: ${counter.current + prop.current}" }
        }
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