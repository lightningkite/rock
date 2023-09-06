package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*


fun ViewContext.allViewTest() {
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
    val formProp = Property(mutableMapOf<String, Any>())
    val switchProp = Property(true)

    val listItems = Property(listOf(1, 2, 3))

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
            listItems set listOf(counter.current, 2, 3, 4, 5)
        }

        switch {
            bind(switchProp)

            nativeText { content = "Switch Label" }
        } in padding(2.rem)

        form {
            bind(formProp) { map ->
                println(map)
            }
            nativeTextField {
                validation = InputValidation(required = true)
                key = "required-field"
                hint = "Required Field"
            }
            row {
                gravity = RowGravity.Center

                nativeText { content = "Primary" }
                nativeButton {
                    nativeText {
                        content = "Submit Form (contained)"
                    }
                    box {
                        image {
                            scaleType = ImageMode.Fit
                            source = ImageRemote("https://picsum.photos/64/64")
                        }
                    } in sizedBox(
                        SizeConstraints(
                            height = 32.px
                        )
                    )
                }
                nativeButton {
                    nativeText {
                        content = "Submit Form (outlined)"
                    }
                }
                nativeButton {
                    nativeText { content = "Submit Form (text)" }
                }
            }
            row {
                gravity = RowGravity.Right

                nativeText { content = "Danger" }
                nativeButton {
                    nativeText { content = "Submit Form (contained)" }
                }
                box {
                    column {
                        gravity = ColumnGravity.Center
                        nativeButton {
                            nativeText { content = "Submit Form (outlined)" }
                        }
                    }
                } in weight(1f)
                nativeButton {
                    nativeText { content = "Submit Form (text)" }
                }
            }
        } in padding(1.rem)

        forEach(
            data = { listItems.current },
            render = { item ->
                nativeText { content = item.toString() } in padding(1.rem)
            }
        )

        box {
            nativeTextField {
                bind(textProp)
                hint = "Some Input Label"
            }
        } in padding(2.rem)

        row {
            nativeActivityIndicator {
                ::color { Color.gray(counter.current / 20f) }
            }
            nativeActivityIndicator {
                ::color { Color.gray(1f - counter.current / 20f) }
            }
        }

        nativeH1 {
            ::content { "Counter: ${counter.current}" }
        }
        nativeText {
            ::content { "This is ${if (counter.current % 2 == 0) "visible" else "invisible"}" }
            ::visible { counter.current % 2 == 0 }
        }

        radioGroup {
            bind(
                options = { listOf("one", "two") },
                prop = radioProp,
                getLabel = { it },
                getKey = { it }
            )
        }

        row {
            alpha = 0.8
            rotation = Angle(-0.015f)
            nativeText {
                textStyle = TextStyle(
                    color = Color.teal,
                    bold = true,
                    italic = true
                )
                ::content { "Lat: ${lat.current}" }
            } in padding(Insets(right = 36.px))
            column {
                nativeText {
                    content = "Hello world"
                }
                nativeText {
                    content = "Hello world 2"
                } in nativeBackground(Background(fill = Color(1f, 1f, 1f, 1f)))
                nativeText {
                    ::content { "Lon: ${lon.current}" }
                } in nativeBackground(Background(fill = Color(1f, 1f, 0f, 0f)))
            }
            nativeText {
                ::content { "This ${if (counter.current % 2 == 0) "exists" else "does not exist"}" }
                ::exists { counter.current % 2 == 0 }
            }
            nativeText { content = "More text" }
        } in nativeBackground(
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
            nativeTextField {
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
        } in padding(4.px) in nativeBackground(
            Background(
                fill = Color.teal,
                corners = CornerRadii(24.px)
            )
        ) in margin(4.rem)


        nativeButton {
            onClick { counter modify { it + 1 } }
            nativeText { content = "Click me" }
        }

        nativeText {
            ::content { "You input: '${textProp.current}'" }
        }

        nativeDropDown {
            bind(
                options = {
                    listOf("Hello", "World")
                },
                getKey = { it.lowercase() },
                getLabel = { it.uppercase() },
                prop = dropdownProp
            )
        }

        autoCompleteTextView {
            label = "Autocomplete"
            bind(
                options = {
                    listOf("Hello", "World")
                },
                getKey = { if (it == "Hello") "HeLlO" else "wOrLd" },
                getLabel = { it.uppercase() },
                prop = dropdownProp
            )
            textStyle = TextStyle(
                color = Color.red
            )
            labelStyle = TextStyle(
                color = Color.blue
            )
        } in padding(2.rem)

        nativeText {
            ::content { "You selected: '${dropdownProp.current}'" }
        }

        checkBox {
            bind(checkedProp)
            nativeText {
                ::content { "I am ${if (checkedProp.current) "" else " not "} checked" }
                ::textStyle {
                    TextStyle(
                        color = if (checkedProp.current) Color.red else Color.green
                    )
                }
            }
        }

        nativeText {
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
        ) in nativeBackground(
            Background(
                fill = Color.teal
            )
        )

        row {
            nativeText { content = "Hello" }
            nativeText { content = "Hello" }
            nativeText { content = "Hello" }
            nativeText { content = "Hello" }
            nativeText { content = "Hello" }
            nativeText { content = "Hello" }
            nativeText { content = "Hello" }
            nativeText { content = "Hello" }
            nativeText { content = "Hello" }
            nativeText { content = "Hello" }
            nativeText { content = "Hello" }
            nativeText { content = "Hello" }
        } in scrollsHorizontally() in sizedBox(
            SizeConstraints(
                minWidth = 128.px,
                maxWidth = 128.px,
                minHeight = 64.px
            )
        ) in nativeBackground(
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

