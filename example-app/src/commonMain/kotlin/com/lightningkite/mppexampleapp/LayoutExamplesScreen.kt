package com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

@Routable("layout-examples")
object LayoutExamplesScreen : RockScreen {
    override fun ViewWriter.render() {
        col {
            h1 { content = "Sampling" }

            col {
                h2 { content = "Stack Layout" }
                stack {
                    val aligns = listOf(Align.Start, Align.Center, Align.End)
                    for (h in aligns) {
                        for (v in aligns) {
                            text { content = "$h $v" } in gravity(h, v)
                        }
                    }
                } in sizedBox(SizeConstraints(minHeight = 200.px))
            } in card

            col {
                h2 { content = "Column Gravity" }
                col {
                    val aligns = listOf(Align.Start, Align.Center, Align.End)
                    for (h in aligns) {
                        text { content = "$h" } in gravity(h, Align.Stretch)
                    }
                }
            } in card

            col {
                h2 { content = "Row Gravity / Weight" }
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
            } in card

            col {
                h2 { content = "Dynamic List" }
                val countString = Property("5")
                row {
                    forEachUpdating(
                        shared {
                            (1..(countString.await().toIntOrNull()
                                ?: 1).coerceAtMost(100)).map { "Item $it" }
                        }
                    ) {
                        text { ::content.invoke { it.await() } }
                    }
                } in scrollsHorizontally
                label {
                    content = "Element count:"
                    textField { content bind countString }
                }
            } in card

            col {
                h2 { content = "Max Size" }
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

            col {
                h2("Scroll text")
                sizeConstraints(height = 10.rem) - scrolls - col {
                    col {
                        sizeConstraints(height = 100.rem) - text("This item is really tall!")
                    }
                }
            }

            col {
                h2 { content = "Max Size / Image Interaction" }
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
        } in scrolls
    }
}
