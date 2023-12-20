package com.lightningkite.mppexampleapp

import com.lightningkite.rock.contains
import com.lightningkite.rock.delay
import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.*
import kotlin.random.Random

fun ViewWriter.iosTest() {
    val theme = M3Theme(
        foreground = Color.white,
        backgroundAdjust = 0.5f,
        primary = Color.blue.toHSV().copy(saturation = 0.5f, value = 0.5f).toRGB(),
        secondary = Color.green.toHSV().copy(saturation = 0.5f, value = 0.5f).toRGB(),
    )
    val incrementing = Property(7)
    col {
        launch {
            while(true) {
                delay(1000L)
                incrementing.value++
            }
        }
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
            } in sizedBox(SizeConstraints(minHeight = 300.px))
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
                    text { content = "$v" } in gravity(Align.Stretch, v)
                }
                stack { text { content = "Weight 1" } in gravity(Align.Center, Align.Center) } in weight(1f)
                for (v in aligns) {
                    text { content = "$v" } in gravity(Align.Stretch, v)
                }
            } in sizedBox(SizeConstraints(minHeight = 300.px))
        } in card

        row {
            text { ::content { incrementing.await().toString() } }
            text { ::content { incrementing.await().toString() } }
            text { ::content { incrementing.await().toString() } }
        } in card

        col {
            h2 { content = "Dynamic List" }
            val countString = Property("5")
            col {
                forEachUpdating(
                    shared {
                        (1..incrementing.await().coerceAtMost(20)).map { "Item $it" }
                    }
                ) {
                    text { ::content.invoke { it.await() } }
                }
                space() in weight(1f)
            } in scrolls in sizedBox(SizeConstraints(maxHeight = 300.px))
//            label {
//                content = "Element count:"
//                textField { content bind countString }
//            }
        } in card

//        col {
//            h2 { content = "Style Test" }
//            stack { text { content = "card" } } in card
//            stack { text { content = "bar" } } in bar
//            stack { text { content = "important" } } in important
//            stack { text { content = "critical" } } in critical
//            stack { text { content = "warning" } } in warning
//            stack { text { content = "danger" } } in danger
//            stack { text { content = "affirmative" } } in affirmative
//        }

        space() in weight(1f)
    } in setTheme {
        theme
    }
}

fun sampleText() = "From Kotlin for iOS"