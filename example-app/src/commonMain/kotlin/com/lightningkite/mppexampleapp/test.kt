package com.lightningkite.mppexampleapp

import com.lightningkite.rock.contains
import com.lightningkite.rock.delay
import com.lightningkite.rock.launch
import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.*

fun ViewContext.testView() {
    val currentTheme = Property<Theme>(MaterialLikeTheme.random())
    col {
        col {
            h1 { TextView_content = "Beautiful by default." }
            text {
                TextView_content =
                    "In Rock, styling is beautiful without effort.  No styling or CSS is required to get beautiful layouts.\n\nJust how it should be."
            }
        }
        row {
            h1 { TextView_content = "Unwrapped" }
            text { TextView_content = "Some Content" }
        }
        row {
            h1 { TextView_content = "Card" }
            text { TextView_content = "Some Content" }
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

        col {
            textField {  }
            textField {  } in card
            checkbox {  } in card
            radioButton {  } in card
            switch {  } in card
        }

        button {
            text { TextView_content = "Alter Themes Randomly for Ten Seconds" }
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