package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLAnchorElement

fun main() {
    val counter = Property(0)
    window.setInterval({
        counter modify { it + 1 }
        println(counter.once)
    }, 1000)
    val context = ViewContext(document.body!!)
    context.myView(counter)
//    with(context) {
////        val prop = Property(1)
////
////        val lat = Property<Double?>(null)
////        val lon = Property<Double?>(null)
////
////        watchGeolocation { pos ->
////            lat set pos.latitude
////            lon set pos.longitude
////        }
////
////        column {
////
////            simpleLabel {
////                text = "Hi!  Welcome! ${counter.once}"
////            }
////            row {
////                simpleLabel {
////                    ::text { "Lat: ${lat.current}" }
////                }
////                simpleLabel {
////                    ::text { "Lon: ${lon.current}" }
////                }
////            }
////            simpleLabel {
////                text = "Hi!  Welcome! ${counter.once}"
////            }
//////
//////            element<HTMLAnchorElement>("a") {
//////                innerText = "Test"
//////                href = "https://google.com"
//////            }
//////
//////            simpleLabel {
//////                ::text { "Counter value: ${counter.current}" }
//////            } in padding() in padding()
//////
//////            padding()
//////            simpleLabel {
//////                ::text { "Counter value plus one: ${counter.current + prop.current}" }
//////            }
//////
//////            simpleLabel {
//////                reactiveScope { text = "Counter value plus one: ${counter.current + prop.current}" }
//////            }
////        }
//    }
}
