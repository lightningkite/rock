package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

fun ViewContext.alignTest() {
    column {
        column {
            text { content = "Test" } in alignLeft() in withBackground(Background(Color.teal))
            text { content = "Test" } in alignCenter() in withBackground(Background(Color.teal))
            text { content = "Test" } in withBackground(Background(Color.teal)) in weight(1f)
            text { content = "Test" } in alignRight() in withBackground(Background(Color.teal))
        } in withBackground(Background(Color.yellow)) in weight(1f)
        row {
            text { content = "Test" } in alignTop() in withBackground(Background(Color.teal))
            text { content = "Test" } in alignCenter() in withBackground(Background(Color.teal))
            text { content = "Test" } in withBackground(Background(Color.teal)) in weight(1f)
            text { content = "Test" } in alignBottom() in withBackground(Background(Color.teal))
        } in sizedBox(SizeConstraints(height = 100.px)) in withBackground(Background(Color.green))
    }
}

fun ViewContext.fetchTest() {
    val myOwnPage = Property("")
    launch {
        myOwnPage set fetch("/").text()
    }
    val barTextStyle = TextStyle(
        color = Color.white,
        size = 18.0,
        bold = true,
        font = Resources.titleFont
    )
    column {
        text {
            content = "You can enable hot reload via adding  --continuous to your run config"
            textStyle = barTextStyle
        } in padding(1.rem) in withBackground(Background(Color.blue))
        text { ::content { myOwnPage.current } }
    }
}

fun ViewContext.asyncTest() {
    val counter = Property(0)
    val prop = Property(1)
    launch {
        timeoutOrNull(5_000) {
            while (true) {
                delay(1000)
                counter.modify { it + 1 }
                async {
                    for (i in 0..5) {
                        delay(500)
                        prop.modify { it + 1 }
                    }
                }
            }
        }
        println("WAIT STOP")
    }
    column {
        row {
            text { ::content { counter.current.toString() } }
            text { content = " + " }
            text { ::content { prop.current.toString() } }
            text { content = " = " }
            text { ::content { prop.current.plus(counter.current).toString() } }
        }
    }
}

fun ViewContext.elementTest() {
    val counter = Property(0)
    launch {
        while (true) {
            delay(1000)
            counter.modify { it + 1 }
            async {
                delay(200)
            }
        }
    }
    val lat = Property<Double?>(null)
    val lon = Property<Double?>(null)

    val textProp = Property("")
    val dropdownProp = Property<String?>(null)
    val checkedProp = Property(false)
    val radioProp = Property("one")

    watchGeolocation { pos ->
        lat set pos.latitude
        lon set pos.longitude
    }

    column {
        reactiveScope {
            if (counter.current == 5) {
                textProp set "FIVE"
                dropdownProp set "World"
                radioProp set "two"
                checkedProp set true
            }
        }

        activityIndicator {

        }

        h1 {
            ::content { "Counter: ${counter.current}" }
        }
        text {
            ::content { "This is ${if (counter.current % 2 == 0) "visible" else "invisible"}" }
            ::visible { counter.current % 2 == 0 }
        }

        radioButton {
            bind(
                options = { listOf("one", "two") },
                prop = radioProp,
                getLabel = { it },
                getKey = { it }
            )
        }

        row {
            alpha = 0.8
            rotation = Angle(-0.05f)
            text {
                textStyle = TextStyle(
                    color = Color.teal,
                    bold = true,
                    italic = true
                )
                ::content { "Lat: ${lat.current}" }
            } in padding(Insets(right = 36.px))
            column {
                text {
                    content = "Hello world"
                }
                text {
                    content = "Hello world 2"
                } in withBackground(Background(fill = Color(1f, 1f, 1f, 1f)))
                text {
                    ::content { "Lon: ${lon.current}" }
                } in withBackground(Background(fill = Color(1f, 1f, 0f, 0f)))
            }
            text {
                ::content { "This ${if (counter.current % 2 == 0) "exists" else "does not exist"}" }
                ::exists { counter.current % 2 == 0 }
            }
            text { content = "More text" }
        } in withBackground(
            Background(
                fill = RadialGradient(
                    stops = listOf(
                        GradientStop(ratio = 0.5f, color = Color(1f, 1f, 0f, 0f)),
                        GradientStop(ratio = 0.6f, color = Color(1f, 0f, 1f, 0f)),
                        GradientStop(ratio = 0.7f, color = Color(1f, 0f, 0f, 1f)),
                    )
                ),
                stroke = Color.purple,
                strokeWidth = 6.px,
                corners = CornerRadii(20.px)
            )
        ) in margin(1.rem)

        box {
            elevation = 7.px
            textField {
                bind(textProp)
                hint = "test 123"
                keyboardHints = KeyboardHints(
                    autocomplete = AutoComplete.Password,
                    action = null,
                    case = KeyboardCase.None,
                    type = KeyboardType.Text
                )
                textStyle = TextStyle(
                    color = Color.green,
                    bold = true
                )
            } in margin(8.px)
        } in padding(4.px) in withBackground(
            Background(
                fill = Color.teal,
                corners = CornerRadii(24.px)
            )
        ) in margin(4.rem)


        button {
            onClick { println("test button") }
            text { content = "Click me" }
        }

        text {
            ::content { "You input: '${textProp.current}'" }
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
            ::content { "You selected: '${dropdownProp.current}'" }
        }

        checkBox {
            bind(checkedProp)
            text {
                ::content { "I am ${if (checkedProp.current) "" else " not "} checked" }
                ::textStyle {
                    TextStyle(
                        color = if (checkedProp.current) Color.red else Color.green
                    )
                }
            }
        }

        text {
            content =
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Pellentesque diam volutpat commodo sed egestas. Arcu odio ut sem nulla pharetra diam. Sit amet commodo nulla facilisi nullam vehicula ipsum a arcu. Tincidunt arcu non sodales neque. Nunc lobortis mattis aliquam faucibus purus in massa tempor nec. Tempor id eu nisl nunc mi ipsum faucibus vitae. Scelerisque purus semper eget duis at tellus at urna. Sed euismod nisi porta lorem mollis aliquam. Gravida rutrum quisque non tellus orci ac. Eu sem integer vitae justo. Pretium nibh ipsum consequat nisl vel pretium lectus quam id. Elit sed vulputate mi sit amet mauris commodo quis imperdiet.\n" +
                        "\n" +
                        "Urna duis convallis convallis tellus id interdum velit laoreet id. Tellus integer feugiat scelerisque varius morbi enim. Volutpat diam ut venenatis tellus in metus vulputate. Pharetra et ultrices neque ornare aenean euismod. Posuere ac ut consequat semper viverra nam. Tempor orci eu lobortis elementum nibh tellus molestie nunc. A scelerisque purus semper eget duis. Cras sed felis eget velit. Orci eu lobortis elementum nibh tellus. Sed blandit libero volutpat sed cras. Tempor commodo ullamcorper a lacus vestibulum sed. A pellentesque sit amet porttitor eget dolor. Viverra mauris in aliquam sem fringilla ut.\n" +
                        "\n" +
                        "Etiam tempor orci eu lobortis elementum nibh. Dolor sit amet consectetur adipiscing elit. Congue eu consequat ac felis donec et odio. Tincidunt vitae semper quis lectus nulla at volutpat diam. Eu tincidunt tortor aliquam nulla. Eget nunc scelerisque viverra mauris. Purus sit amet luctus venenatis lectus magna. Nunc mattis enim ut tellus elementum sagittis vitae et. Gravida neque convallis a cras. Accumsan tortor posuere ac ut consequat semper viverra. Amet consectetur adipiscing elit duis tristique sollicitudin nibh. At varius vel pharetra vel turpis nunc eget. Urna neque viverra justo nec ultrices dui sapien eget. Ut etiam sit amet nisl purus in mollis nunc. Quis risus sed vulputate odio ut enim blandit. Varius duis at consectetur lorem donec massa sapien faucibus. Sit amet mattis vulputate enim nulla aliquet porttitor.\n" +
                        "\n" +
                        "Cursus euismod quis viverra nibh cras pulvinar mattis nunc. Venenatis cras sed felis eget velit aliquet sagittis id consectetur. In hac habitasse platea dictumst quisque sagittis purus. Tellus rutrum tellus pellentesque eu tincidunt tortor aliquam nulla facilisi. Pellentesque adipiscing commodo elit at imperdiet. Dui id ornare arcu odio ut sem nulla. Habitant morbi tristique senectus et netus et malesuada. Mauris commodo quis imperdiet massa tincidunt. Ipsum a arcu cursus vitae congue mauris rhoncus aenean. Bibendum ut tristique et egestas quis ipsum suspendisse ultrices gravida.\n" +
                        "\n" +
                        "Non curabitur gravida arcu ac tortor dignissim convallis aenean. Luctus venenatis lectus magna fringilla urna porttitor rhoncus dolor. Nisi quis eleifend quam adipiscing vitae proin sagittis nisl rhoncus. Habitant morbi tristique senectus et netus et malesuada fames ac. Iaculis urna id volutpat lacus laoreet non curabitur gravida. Habitant morbi tristique senectus et netus et malesuada. Facilisi nullam vehicula ipsum a arcu cursus. Orci a scelerisque purus semper eget duis. Urna cursus eget nunc scelerisque. Blandit volutpat maecenas volutpat blandit aliquam etiam erat. Id interdum velit laoreet id donec ultrices. Tristique nulla aliquet enim tortor at auctor urna nunc. Enim ut sem viverra aliquet eget. Amet volutpat consequat mauris nunc congue nisi vitae suscipit. Tempus imperdiet nulla malesuada pellentesque elit. Vulputate mi sit amet mauris commodo quis imperdiet."
        } in scrolls() in sizedBox(
            SizeConstraints(
                minHeight = 64.px
            )
        ) in withBackground(
            Background(
                fill = Color.teal
            )
        )

        row {
            text { content = "Hello" }
            text { content = "Hello" }
            text { content = "Hello" }
            text { content = "Hello" }
            text { content = "Hello" }
            text { content = "Hello" }
            text { content = "Hello" }
            text { content = "Hello" }
            text { content = "Hello" }
            text { content = "Hello" }
            text { content = "Hello" }
            text { content = "Hello" }
        } in scrollsHorizontally() in sizedBox(
            SizeConstraints(
                minWidth = 128.px,
                maxWidth = 128.px,
                minHeight = 64.px
            )
        ) in withBackground(
            Background(
                fill = Color.blue
            )
        )

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

//        webView {
//            url = "http://localhost:8080"
//        } in sizedBox(
//            SizeConstraints(
//                width = 512.px,
//                height = 512.px
//            )
//        )
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