package com.lightningkite.mppexampleapp

import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.bind
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.*

fun ViewContext.componentDemo() {
    val currentTheme = Property<Theme>(MaterialLikeTheme())

    val stringContent = Property("Test")
    val booleanContent = Property(false)

    col {
        row {
            h2 { TextView_content = "Top Bar Example" } in weight(1f) in gravity(Align.Center, Align.Center)
            button { image { this.Image_source = Icons.search.color(Color.white) } }
        } in important in bordering
        col {
            h1 { TextView_content = "Beautiful by default." }
            text {
                TextView_content =
                    "In Rock, styling is beautiful without effort.  No styling or CSS is required to get beautiful layouts.\n\nJust how it should be."
            }
        } in withPadding

        col {
            h2 { TextView_content = "Theme Control" }
            row {
                button {
                    h6 { TextView_content = "M1 Light" }
                    onClick {
                        currentTheme set MaterialLikeTheme.randomLight().randomElevationAndCorners().randomTitleFontSettings()
                    }
                } in important
                button {
                    h6 { TextView_content = "M1 Dark" }
                    onClick {
                        currentTheme set MaterialLikeTheme.randomDark().randomElevationAndCorners().randomTitleFontSettings()
                    }
                } in important
                button {
                    h6 { TextView_content = "M3 Light" }
                    onClick {
                        currentTheme set M3Theme.randomLight().randomElevationAndCorners().randomTitleFontSettings()
                    }
                } in important
                button {
                    h6 { TextView_content = "M3 Dark" }
                    onClick {
                        currentTheme set M3Theme.randomDark().randomElevationAndCorners().randomTitleFontSettings()
                    }
                } in important
            }
        } in card

        col {
            h2 { TextView_content = "Buttons" }
            row {
                space {} in weight(1f)
                button { text { TextView_content = "Sample" } }
                button { text { TextView_content = "Card" } } in card
                button { text { TextView_content = "Important" } } in important
                button { text { TextView_content = "Critical" } } in critical
                button { text { TextView_content = "Warning" } } in warning
                button { text { TextView_content = "Danger" } } in danger
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
            h2 { TextView_content = "Toggle Buttons" }
            row {
                space {} in weight(1f)
                toggleButton { ToggleButton_checked bind booleanContent; text { TextView_content = "Sample" } }
                toggleButton { ToggleButton_checked bind booleanContent; text { TextView_content = "Card" } } in card
                toggleButton { ToggleButton_checked bind booleanContent; text { TextView_content = "Important" } } in important
                toggleButton { ToggleButton_checked bind booleanContent; text { TextView_content = "Critical" } } in critical
                space {} in weight(1f)
            }
        } in card

        col {
            h2 { TextView_content = "Switches" }
            row {
                space {} in weight(1f)
                switch { Switch_checked bind booleanContent; }
                switch { Switch_checked bind booleanContent; } in card
                switch { Switch_checked bind booleanContent; } in important
                switch { Switch_checked bind booleanContent; } in critical
                space {} in weight(1f)
            }
        } in card

        col {
            h2 { TextView_content = "Text Fields" }
            textField { TextField_content bind stringContent }
            textField { TextField_content bind stringContent } in card
            textField { TextField_content bind stringContent } in important
            textField { TextField_content bind stringContent } in critical
        } in card

        col {
            h2 { TextView_content = "Text Areas" }
            textArea { TextArea_content bind stringContent }
            textArea { TextArea_content bind stringContent } in card
            textArea { TextArea_content bind stringContent } in important
            textArea { TextArea_content bind stringContent } in critical
        } in card

        col {
            h2 { TextView_content = "Images" }
            image { Image_source = ImageRemote("https://picsum.photos/200/300") } in sizedBox(SizeConstraints(maxWidth = 100.px, minWidth = 100.px))
        } in card

        col {
            h2 { TextView_content = "Stack Layout" }
            stack {
                val aligns = listOf(Align.Start, Align.Center, Align.End)
                for(h in aligns) {
                    for (v in aligns) {
                        text { TextView_content = "$h $v" } in gravity(h, v)
                    }
                }
            } in sizedBox(SizeConstraints(minHeight = 200.px))
        } in card

        col {
            h2 { TextView_content = "Column Gravity" }
            col {
                val aligns = listOf(Align.Start, Align.Center, Align.End)
                for(h in aligns) {
                    text { TextView_content = "$h" } in gravity(h, Align.Stretch)
                }
            }
        } in card

        col {
            h2 { TextView_content = "Row Gravity / Weight" }
            row {
                val aligns = listOf(Align.Start, Align.Center, Align.End)
                for(v in aligns) {
                    text { TextView_content = "$v" } in gravity(Align.Stretch, v)
                }
                stack { text { TextView_content = "Weight 1" } in gravity(Align.Center, Align.Center) } in weight(1f)
                for(v in aligns) {
                    text { TextView_content = "$v" } in gravity(Align.Stretch, v)
                }
            } in sizedBox(SizeConstraints(minHeight = 200.px))
        } in card

    } in scrolls() in setTheme { currentTheme.current } in bordering

}