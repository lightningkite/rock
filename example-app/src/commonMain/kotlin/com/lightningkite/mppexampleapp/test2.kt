package com.lightningkite.mppexampleapp

import com.lightningkite.rock.contains
import com.lightningkite.rock.delay
import com.lightningkite.rock.launch
import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.bind
import com.lightningkite.rock.reactive.equalTo
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.*

fun ViewContext.testView() {
    val currentTheme = Property<Theme>(MaterialLikeTheme())
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
        stack {
            val aligns = listOf(Align.Start, Align.Center, Align.End)
            for(h in aligns) {
                for (v in aligns) {
                    text { content = "$h $v" } in gravity(h, v)
                }
            }
        } in sizedBox(SizeConstraints(minHeight = 200.px))
        row {
            val current = Property(0)
            repeat(5) {
                radioToggleButton {
                    h6 { content = "B$it" }
                    checked bind (current equalTo it)
                } in weight(1f)
            }
        }
        row {
            h1 { content = "Unwrapped" }
            text { content = "Some Content" }
        } in withPadding
        row {
            h1 { content = "Card" }
            text { content = "Some Content" } in gravity(Align.Center, Align.End)
        } in card
        row {
            h1 { content = "Important" }
            text { content = "Some Content" }
        } in important
        row {
            h1 { content = "Critical" }
            text { content = "Some Content" }
        } in critical
        row {
            h1 { content = "Warning" }
            text { content = "Some Content" }
        } in warning
        row {
            h1 { content = "Danger" }
            text { content = "Some Content" }
        } in danger

        col {
            h1 { content = "Important" }
            row {
                h1 { content = "Important Important" }
                text { content = "Some Content" }
            } in important
        } in important

        val textData = Property("Test Text")
        val checked = Property(false)

        col {
            textField { this.content bind textData }
            textField { this.content bind textData } in card
            row {
                checkbox { this.checked bind checked } in card
                radioButton { this.checked bind checked } in card
                switch { checked bind checked } in card
                toggleButton {
                    checked bind checked
                    h6 { content = "Toggle" }
                } in card
                radioToggleButton {
                    checked bind checked
                    h6 { content = "On Only" }
                } in card
            }
            textArea { this.content bind textData } in card
            autoCompleteTextField {
                this.content bind textData
                this.suggestions = listOf("Apple", "Banana", "Crepe")
            } in card
            dropDown {
                this.options = listOf("Apple", "Banana", "Crepe").map { WidgetOption(it, it) }
//                this.selected bind textData
            } in card
        }

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
        button {
            h6 { content = "Alter Themes Randomly for Ten Seconds" }
            onClick {
                launch {
                    repeat(10) {
                        currentTheme set M3Theme.random().randomElevationAndCorners().randomTitleFontSettings()
                        delay(1000)
                    }
                }
            }
        } in important
    } in scrolls() in setTheme { currentTheme.current } in bordering

}