package com.lightningkite.mppexampleapp

import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.SharedReadable
import com.lightningkite.rock.reactive.bind
import com.lightningkite.rock.reactive.invoke
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.*

fun ViewContext.componentDemo() {
    val currentTheme = Property<Theme>(MaterialLikeTheme())

    val stringContent = Property("Test")
    val booleanContent = Property(false)

    col {
        row {
            h2 { content = "Top Bar Example" } in weight(1f) in gravity(Align.Center, Align.Center)
            button { image { this.source = Icons.search.color(Color.white) } }
        } in important in bordering
        col {
            h1 { content = "Beautiful by default." }
            text {
                content =
                    "In Rock, styling is beautiful without effort.  No styling or CSS is required to get beautiful layouts.\n\nJust how it should be."
            }
        } in withPadding

        col {
            h2 { content = "Theme Control" }
            row {
                button {
                    h6 { content = "M1 Light" }
                    onClick {
                        currentTheme set MaterialLikeTheme.randomLight().randomElevationAndCorners().randomTitleFontSettings()
                    }
                } in important
                button {
                    h6 { content = "M1 Dark" }
                    onClick {
                        currentTheme set MaterialLikeTheme.randomDark().randomElevationAndCorners().randomTitleFontSettings()
                    }
                } in important
                button {
                    h6 { content = "M3 Light" }
                    onClick {
                        currentTheme set M3Theme.randomLight().randomElevationAndCorners().randomTitleFontSettings()
                    }
                } in important
                button {
                    h6 { content = "M3 Dark" }
                    onClick {
                        currentTheme set M3Theme.randomDark().randomElevationAndCorners().randomTitleFontSettings()
                    }
                } in important
            }
        } in card

        col {
            h2 { content = "Buttons" }
            row {
                space {} in weight(1f)
                button { text { content = "Sample" } }
                button { text { content = "Card" } } in card
                button { text { content = "Important" } } in important
                button { text { content = "Critical" } } in critical
                button { text { content = "Warning" } } in warning
                button { text { content = "Danger" } } in danger
                space {} in weight(1f)
            }
        } in card /*themeFromLast {
            it.copy(
                foreground = Color.red,
                outline = Color.red,
                outlineWidth = 4.px,
            )
        }*/

        col {
            h2 { content = "Toggle Buttons" }
            row {
                space {} in weight(1f)
                toggleButton { checked bind booleanContent; text { content = "Sample" } }
                toggleButton { checked bind booleanContent; text { content = "Card" } } in card
                toggleButton { checked bind booleanContent; text { content = "Important" } } in important
                toggleButton { checked bind booleanContent; text { content = "Critical" } } in critical
                space {} in weight(1f)
            }
        } in card

        col {
            h2 { content = "Switches" }
            row {
                space {} in weight(1f)
                switch { checked bind booleanContent; }
                switch { checked bind booleanContent; } in card
                switch { checked bind booleanContent; } in important
                switch { checked bind booleanContent; } in critical
                space {} in weight(1f)
            }
        } in card

        col {
            h2 { content = "Activity Indicators" }
            row {
                space {} in weight(1f)
                stack { activityIndicator { } } in withPadding
                stack { activityIndicator { } } in card
                stack { activityIndicator { } } in important
                stack { activityIndicator { } } in critical
                stack { activityIndicator { } } in warning
                stack { activityIndicator { } } in danger
                space {} in weight(1f)
            }
        } in card

        col {
            h2 { content = "Drop Downs" }
            val options = listOf("Apple", "Banana", "Crepe").map { WidgetOption(it, it) }
            dropDown { this.options = options } in withPadding
            dropDown { this.options = options } in card
            dropDown { this.options = options } in important
            dropDown { this.options = options } in critical
            dropDown { this.options = options } in warning
            dropDown { this.options = options } in danger
        } in card

        col {
            h2 { content = "Text Fields" }
            textField { content bind stringContent }
            textField { content bind stringContent } in card
            textField { content bind stringContent } in important
            textField { content bind stringContent } in critical
        } in card

        col {
            h2 { content = "Text Areas" }
            textArea { content bind stringContent }
            textArea { content bind stringContent } in card
            textArea { content bind stringContent } in important
            textArea { content bind stringContent } in critical
        } in card

        col {
            h2 { content = "Images" }
            row {
                repeat(5) {
                    image { source = ImageRemote("https://picsum.photos/seed/${it}/200/300") } in sizedBox(
                        SizeConstraints(
                            maxWidth = 100.px,
                            minWidth = 100.px
                        )
                    )
                }
            }
        } in card

        col {
            h2 { content = "Stack Layout" }
            stack {
                val aligns = listOf(Align.Start, Align.Center, Align.End)
                for(h in aligns) {
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
                for(h in aligns) {
                    text { content = "$h" } in gravity(h, Align.Stretch)
                }
            }
        } in card

        col {
            h2 { content = "Row Gravity / Weight" }
            row {
                val aligns = listOf(Align.Start, Align.Center, Align.End)
                for(v in aligns) {
                    text { content = "$v" } in gravity(Align.Stretch, v)
                }
                stack { text { content = "Weight 1" } in gravity(Align.Center, Align.Center) } in weight(1f)
                for(v in aligns) {
                    text { content = "$v" } in gravity(Align.Stretch, v)
                }
            } in sizedBox(SizeConstraints(minHeight = 200.px))
        } in cardD

        col {
            h2 { content = "Dynamic List" }
            val countString = Property("5")
            row {
                forEachUpdating(
                    SharedReadable { (1 .. (countString.current.toIntOrNull() ?: 0).coerceAtMost(100)).map { "Item $it" } }
                ) {
                    text { ::content.invoke { it.current } }
                }
            } in scrollsHorizontally()
            text { content = "Element count:" }
            textField { content bind countString }
        } in card

    } in scrolls() in setTheme { currentTheme.current } in bordering

}