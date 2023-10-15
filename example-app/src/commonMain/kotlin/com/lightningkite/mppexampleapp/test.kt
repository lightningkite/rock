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
        stack {
            val aligns = listOf(Align.Start, Align.Center, Align.End)
            for(h in aligns) {
                for (v in aligns) {
                    text { TextView_content = "$h $v" } in gravity(h, v)
                }
            }
        } in sizedBox(SizeConstraints(minHeight = 200.px))
        row {
            val current = Property(0)
            repeat(5) {
                radioToggleButton {
                    h6 { TextView_content = "B$it" }
                    RadioToggleButton_checked bind (current equalTo it)
                } in weight(1f)
            }
        }
        row {
            h1 { TextView_content = "Unwrapped" }
            text { TextView_content = "Some Content" }
        } in withPadding
        row {
            h1 { TextView_content = "Card" }
            text { TextView_content = "Some Content" } in gravity(Align.Center, Align.End)
        } in card
        row {
            h1 { TextView_content = "Important" }
            text { TextView_content = "Some Content" }
        } in important
        row {
            h1 { TextView_content = "Critical" }
            text { TextView_content = "Some Content" }
        } in critical
        row {
            h1 { TextView_content = "Warning" }
            text { TextView_content = "Some Content" }
        } in warning
        row {
            h1 { TextView_content = "Danger" }
            text { TextView_content = "Some Content" }
        } in danger

        col {
            h1 { TextView_content = "Important" }
            row {
                h1 { TextView_content = "Important Important" }
                text { TextView_content = "Some Content" }
            } in important
        } in important

        val textData = Property("Test Text")
        val checked = Property(false)

        col {
            textField { this.TextField_content bind textData }
            textField { this.TextField_content bind textData } in card
            row {
                checkbox { this.Checkbox_checked bind checked } in card
                radioButton { this.RadioButton_checked bind checked } in card
                switch { Switch_checked bind checked } in card
                toggleButton {
                    ToggleButton_checked bind checked
                    h6 { TextView_content = "Test" }
                } in important
                radioToggleButton {
                    RadioToggleButton_checked bind checked
                    h6 { TextView_content = "Test" }
                } in important
            }
            textArea { this.TextArea_content bind textData } in card
            autoCompleteTextField {
                this.AutoCompleteTextField_content bind textData
                this.AutoCompleteTextField_suggestions = listOf("Apple", "Banana", "Crepe")
            } in card
            dropDown {
                this.DropDown_options = listOf("Apple", "Banana", "Crepe").map { WidgetOption(it, it) }
//                this.DropDown_selected bind textData
            } in card
        }

        button {
            h6 { TextView_content = "Alter Theme" }
            onClick {
                currentTheme set MaterialLikeTheme.random().randomElevationAndCorners().randomTitleFontSettings()
            }
        } in important
        button {
            h6 { TextView_content = "Alter Themes Randomly for Ten Seconds" }
            onClick {
                launch {
                    repeat(10) {
                        currentTheme set MaterialLikeTheme.random().randomElevationAndCorners().randomTitleFontSettings()
                        delay(1000)
                    }
                }
            }
        } in important
    } in scrolls() in setTheme { currentTheme.current }

}