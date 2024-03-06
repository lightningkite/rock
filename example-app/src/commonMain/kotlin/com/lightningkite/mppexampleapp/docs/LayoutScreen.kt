package com.lightningkite.mppexampleapp.docs

import com.lightningkite.rock.Routable
import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

@Routable("docs/layout")
object LayoutScreen: DocScreen {
    override val covers: List<String> = listOf("layout", "Layout")

    override fun ViewWriter.render() {
        article {
            h1("Layout")
            h2("Column")
            text("The column layout is very simple it places the items vertically across the view.")
            example("""
                card -col {
                    text("Item 1")
                    text("Item 2")
                    text("Item 3")
                }
            """.trimIndent()) {
                card - col {
                    text("This is a column")
                    text("This is a column")
                }
            }

            h2("Row")
            text("The row layout is the same as the column layout, but it places the items horizontally across the view.")
            example("""
                card - row {
                    text("Item 1")
                    text("Item 2")
                    text("Item 3")
                }
            """.trimIndent()) {
                card - row {
                    text("Item 1")
                    text("Item 2")
                    text("Item 3")
                }
            }

            h2("Stack Layout")
            text("Stack layout allows for items to be stacked on top of each other.  This is useful for creating overlays. The last item in the stack is on top. This could hide views behind other views")
            example("""
                card - col {
                    stack {
                        text("Last Item")
                        compact - card - text("Behind ") in tweakTheme {  it.copy(foreground = Color(0.2f, 0f, 0f, 1f)) }
                        text("In front") in tweakTheme {  it.copy(foreground = Color(1f, 0f, 0.2f, 1f)) }
                    }
                }
            """.trimIndent()) {
                card - col {
                    stack {
                        text("Last Item")
                        compact - card - text("Behind ") in tweakTheme {  it.copy(foreground = Color(0.2f, 0f, 0f, 1f)) }
                        text("In front") in tweakTheme {  it.copy(foreground = Color(1f, 0f, 0.2f, 1f)) }
                    }
                }
            }
            text("Using the gravity modifier with the stack layout, you can align items within the stack.")
            example(
                """
            card - col {
                stack {
                    val aligns = listOf(Align.Start, Align.Center, Align.End)
                    for (h in aligns) {
                        for (v in aligns) {
                            text { content = "$\h $\v" } in gravity(h, v)
                        }
                    }
                } in sizedBox(SizeConstraints(minHeight = 200.px))
            }
                """.trimIndent()
            ) {
                card - col {
                    stack {
                        val aligns = listOf(Align.Start, Align.Center, Align.End)
                        for (h in aligns) {
                            for (v in aligns) {
                                text { content = "$h $v" } in gravity(h, v)
                            }
                        }
                    } in sizedBox(SizeConstraints(minHeight = 200.px))
                }
            }



            h2("Column Gravity")
            text("Using the gravity modifier, you can align items within a column.")
            example(
                """
                card - col {
                col {
                    val aligns = listOf(Align.Start, Align.Center, Align.End)
                    for (h in aligns) {
                        text { content = "$/h" } in gravity(h, Align.Stretch)
                    }
                }
            }
            """.trimIndent()
            ) {
                card - col {
                    col {
                        val aligns = listOf(Align.Start, Align.Center, Align.End)
                        for (h in aligns) {
                            text { content = "$h" } in gravity(h, Align.Stretch)
                        }
                    }
                }
            }

            h2("Row Gravity / Weight")

            example(
                """
                            card - col {
                                row {
                                    val aligns = listOf(Align.Start, Align.Center, Align.End)
                                    for (v in aligns) {
                                        gravity(Align.Stretch, v) - text { content = "$/v" }
                                    }
                                    expanding - card - stack {
                                        centered - text { content = "Expanding" }
                                    }
                                    for (v in aligns) {
                                        gravity(Align.Stretch, v) - text { content = "$/v" }
                                    }
                                } in sizedBox(SizeConstraints(minHeight = 200.px))
                            }
                
            """.trimIndent()
            ) {
                card - col {
                    row {
                        val aligns = listOf(Align.Start, Align.Center, Align.End)
                        for (v in aligns) {
                            gravity(Align.Stretch, v) - text { content = "$v" }
                        }
                        expanding - card - stack {
                            centered - text { content = "Expanding" }
                        }
                        for (v in aligns) {
                            gravity(Align.Stretch, v) - text { content = "$v" }
                        }
                    } in sizedBox(SizeConstraints(minHeight = 200.px))
                }
            }


            h2("Dynamic List")
            example(
                """
                card - col {
                val countString = Property("5")
                scrollsHorizontally - row {
                    forEachUpdating(
                        shared {
                            (1..(countString.await().toIntOrNull()
                                ?: 1).coerceAtMost(100)).map { "Item $/it" }
                        }
                    ) {
                        text { ::content.invoke { it.await() } }
                    }
                }
                label {
                    content = "Element count:"
                    textField { content bind countString }
                }
            }
            """.trimIndent()
            ) {
                card - col {
                    val countString = Property("5")
                    scrollsHorizontally - row {
                        forEachUpdating(
                            shared {
                                (1..(countString.await().toIntOrNull()
                                    ?: 1).coerceAtMost(100)).map { "Item $it" }
                            }
                        ) {
                            text { ::content.invoke { it.await() } }
                        }
                    }
                    label {
                        content = "Element count:"
                        textField { content bind countString }
                    }
                }

            }

            h2("Max Size")
            example(
                """
                            card - col {
                val text = Property(true)
                important - button {
                    text("Toggle text size")
                    onClick {
                        text.value = !text.value
                    }
                }
                gravity(Align.Start, Align.Start) - sizeConstraints(maxWidth = 40.rem) - important - stack {
                    text { ::content { if(text.await()) "maxWidth = 40.rem with a lot of additional content to demonstrate large sizes.  Try adjusting the screen width smaller." else "maxWidth = 20.rem" }}
                }
                gravity(Align.Start, Align.Start) - sizeConstraints(width = 40.rem) - important - stack {
                    text { ::content { if(text.await()) "width = 40.rem with a lot of additional content to demonstrate large sizes.  Try adjusting the screen width smaller." else "width = 20.rem" }}
                }
                gravity(Align.Start, Align.Start) - sizeConstraints(minWidth = 40.rem) - important - stack {
                    text { ::content { if(text.await()) "minWidth = 40.rem with a lot of additional content to demonstrate large sizes.  Try adjusting the screen width smaller." else "minWidth = 20.rem" }}
                }
            }
            """.trimIndent()
            ) {
                card - col {
                    val text = Property(true)
                    important - button {
                        text("Toggle text size")
                        onClick {
                            text.value = !text.value
                        }
                    }
                    gravity(Align.Start, Align.Start) - sizeConstraints(maxWidth = 40.rem) - important - stack {
                        text { ::content { if (text.await()) "maxWidth = 40.rem with a lot of additional content to demonstrate large sizes.  Try adjusting the screen width smaller." else "maxWidth = 20.rem" } }
                    }
                    gravity(Align.Start, Align.Start) - sizeConstraints(width = 40.rem) - important - stack {
                        text { ::content { if (text.await()) "width = 40.rem with a lot of additional content to demonstrate large sizes.  Try adjusting the screen width smaller." else "width = 20.rem" } }
                    }
                    gravity(Align.Start, Align.Start) - sizeConstraints(minWidth = 40.rem) - important - stack {
                        text { ::content { if (text.await()) "minWidth = 40.rem with a lot of additional content to demonstrate large sizes.  Try adjusting the screen width smaller." else "minWidth = 20.rem" } }
                    }
                }
            }


            h2("Scroll Text")
            example(
                """
            card - col {
                sizeConstraints(height = 10.rem) - scrolls - col {
                    col {
                        sizeConstraints(height = 100.rem) - text("This item is really tall! Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                    }
                }
            }
            """.trimIndent()
            ) {
                card - col {
                    sizeConstraints(height = 10.rem) - scrolls - col {
                        col {
                            sizeConstraints(height = 100.rem) - text("This item is really tall! Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                        }
                    }
                }
            }


            h2("Compact Test")
            example(
                """
                                card - col {
                h2("Compact test")
                card - compact - col {
                    text("This one is compact")
                    text("This one is compact")
                }
                card - col {
                    text("This one is NOT compact")
                    text("This one is NOT compact")
                }
            }
            """.trimIndent()
            ) {
                card - col {
                    h2("Compact test")
                    card - compact - col {
                        text("This one is compact")
                        text("This one is compact")
                    }
                    card - col {
                        text("This one is NOT compact")
                        text("This one is NOT compact")
                    }
                }
            }



            h2("Custom Spacing Test")
            example(
                """
                            card - col {
                                val showExtra = Property(false)
                                row {
                                    checkbox { checked bind showExtra }
                                    text("Show extra view")
                                }
                                card - row {
                                    spacing = 0.rem
                                    text("0.0")
                                    important - stack { space() }
                                    onlyWhen { showExtra.await() } - important - stack {
                                        space()
                                    }
                                    important - stack { space() }
                                }
                                card - row {
                                    spacing = 0.5.rem
                                    text("0.5")
                                    important - stack { space() }
                                    onlyWhen { showExtra.await() } - important - stack {
                                        space()
                                    }
                                    important - stack { space() }
                                }
                                card - row {
                                    spacing = 1.rem
                                    text("1.0")
                                    important - stack { space() }
                                    onlyWhen { showExtra.await() } - important - stack {
                                        space()
                                    }
                                    important - stack { space() }
                                }
                                card - row {
                                    spacing = 2.rem
                                    text("2.0")
                                    important - stack { space() }
                                    onlyWhen { showExtra.await() } - important - stack {
                                        space()
                                    }
                                    important - stack { space() }
                                }
                                card - button {
                                    spacing = 0.rem
                                    text("spacing = 0.rem")
                                }
                                card - button {
                                    spacing = 0.5.rem
                                    text("spacing = 0.5.rem")
                                }
                                card - button {
                                    spacing = 1.rem
                                    text("spacing = 1.rem")
                                }
                                card - button {
                                    spacing = 2.rem
                                    text("spacing = 2.rem")
                                }
                            }
                
            """.trimIndent()
            ) {
                card - col {
                    val showExtra = Property(false)
                    row {
                        checkbox { checked bind showExtra }
                        text("Show extra view")
                    }
                    card - row {
                        spacing = 0.rem
                        text("0.0")
                        important - stack { space() }
                        onlyWhen { showExtra.await() } - important - stack {
                            space()
                        }
                        important - stack { space() }
                    }
                    card - row {
                        spacing = 0.5.rem
                        text("0.5")
                        important - stack { space() }
                        onlyWhen { showExtra.await() } - important - stack {
                            space()
                        }
                        important - stack { space() }
                    }
                    card - row {
                        spacing = 1.rem
                        text("1.0")
                        important - stack { space() }
                        onlyWhen { showExtra.await() } - important - stack {
                            space()
                        }
                        important - stack { space() }
                    }
                    card - row {
                        spacing = 2.rem
                        text("2.0")
                        important - stack { space() }
                        onlyWhen { showExtra.await() } - important - stack {
                            space()
                        }
                        important - stack { space() }
                    }
                    card - button {
                        spacing = 0.rem
                        text("spacing = 0.rem")
                    }
                    card - button {
                        spacing = 0.5.rem
                        text("spacing = 0.5.rem")
                    }
                    card - button {
                        spacing = 1.rem
                        text("spacing = 1.rem")
                    }
                    card - button {
                        spacing = 2.rem
                        text("spacing = 2.rem")
                    }
                }
            }

            h2("Max Size / Image Interaction")

        example("""
                        card - col {
                            image {
                                source = ImageRemote("https://picsum.photos/seed/test/1920/1080")
                            } in sizedBox(
                                SizeConstraints(
                                    maxHeight = 10.rem
                                )
                            )
                            image {
                                source = ImageRemote("https://picsum.photos/seed/test/600/300")
                            } in sizedBox(
                                SizeConstraints(
                                    maxHeight = 10.rem
                                )
                            )
                            image {
                                source = ImageRemote("https://picsum.photos/seed/test/600/300")
                            } in sizedBox(
                                SizeConstraints(
                                    height = 10.rem
                                )
                            )
                            image {
                                source = ImageRemote("https://picsum.photos/seed/test/600/300")
                                scaleType = ImageScaleType.Crop
                            } in sizedBox(
                                SizeConstraints(
                                    width = 10.rem,
                                    height = 10.rem
                                )
                            )  in centered
                            image {
                                source = ImageRemote("https://picsum.photos/seed/test/600/300")
                                scaleType = ImageScaleType.Fit
                            } in sizedBox(
                                SizeConstraints(
                                    width = 10.rem,
                                    height = 10.rem
                                )
                            )  in centered
                        } in card
        """.trimIndent()){
            card - col {
                image {
                    source = ImageRemote("https://picsum.photos/seed/test/1920/1080")
                } in sizedBox(
                    SizeConstraints(
                        maxHeight = 10.rem
                    )
                )
                image {
                    source = ImageRemote("https://picsum.photos/seed/test/600/300")
                } in sizedBox(
                    SizeConstraints(
                        maxHeight = 10.rem
                    )
                )
                image {
                    source = ImageRemote("https://picsum.photos/seed/test/600/300")
                } in sizedBox(
                    SizeConstraints(
                        height = 10.rem
                    )
                )
                image {
                    source = ImageRemote("https://picsum.photos/seed/test/600/300")
                    scaleType = ImageScaleType.Crop
                } in sizedBox(
                    SizeConstraints(
                        width = 10.rem,
                        height = 10.rem
                    )
                )  in centered
                image {
                    source = ImageRemote("https://picsum.photos/seed/test/600/300")
                    scaleType = ImageScaleType.Fit
                } in sizedBox(
                    SizeConstraints(
                        width = 10.rem,
                        height = 10.rem
                    )
                )  in centered
            } in card
        }
        }
        }
    }
