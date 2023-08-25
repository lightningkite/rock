package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*
import kotlinx.serialization.json.Json

fun ViewContext.myView(counter: Readable<Int>) {
    val prop = Property(1)
    val lat = Property<Double?>(null)
    val lon = Property<Double?>(null)

    val textProp = Property("")
    val dropdownProp = Property<String?>(null)
    val checkedProp = Property(false)

    val apiResponse = Property<String?>(null)

    watchGeolocation { pos ->
        lat set pos.latitude
        lon set pos.longitude
    }

    column {
        text {
            ::text { "Api Response: ${apiResponse.current}" }
        }

        text {
            ::text { "Counter: ${counter.current}" }
        }
        text {
            text = "This is outside the row"
            gravity = TextGravity.Center
        }
        row {
            text {
                textStyle = TextStyle(
                    color = Color.teal,
                    bold = true,
                    italic = true
                )
                ::text { "Lat: ${lat.current}" }
            } in padding(Insets(right = 36.px))
            column {
                text {
                    text = "Hello world"
                }
                text {
                    text = "Hello world 2"
                } in withBackground(Background(fill = Color(1f, 1f, 1f, 1f)))
                text {
                    ::text { "Lon: ${lon.current}" }
                } in withBackground(Background(fill = Color(1f, 1f, 0f, 0f)))
            }
        } in withBackground(
            Background(
                fill = RadialGradient(

                    stops = listOf(
                        GradientStop(ratio = 0.5f, color = Color(1f, 1f, 0f, 0f)),
                        GradientStop(ratio = 0.6f, color = Color(1f, 0f, 1f, 0f)),
                        GradientStop(ratio = 0.7f, color = Color(1f, 0f, 0f, 1f)),
                    )
                )
            )
        )
        editText {
            bind(textProp)
            hint = "test 123"
        }

        button {
            onClick {
                println("test button")
            }
            text = "click me"
        }

        text {
            ::text { "You input: '${textProp.current}'" }
        }

        dropDown {
            bind(
                options = {
                    listOf("Hello", "World")
                },
                getKey = { it.lowercase() },
                getLabel = { it.uppercase() },
                prop = dropdownProp
            )
        }

        text {
            ::text { "You selected: '${dropdownProp.current}'" }
        }

        checkBox {
            bind(checkedProp)
        }

        text {
            ::text { "I am ${if (checkedProp.current) "" else " not "} checked" }
            ::textStyle {
                TextStyle(
                    color = if (checkedProp.current) Color.red else Color.green
                )
            }
        }

        text {
            ::text { "This is ${if (counter.current % 2 == 0) "visible" else "invisible"}" }
            ::visible { counter.current % 2 == 0 }
        }

        box {
            image {
                scaleType = ImageMode.Fit
                source = ImageRemote("https://picsum.photos/200/300")
            }
        } in sizedBox(
            SizeConstraints(
                height = 400.px
            )
        )
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