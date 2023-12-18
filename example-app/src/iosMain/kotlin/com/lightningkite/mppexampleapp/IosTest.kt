package com.lightningkite.mppexampleapp

import com.lightningkite.rock.contains
import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.models.px
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.card
import com.lightningkite.rock.views.direct.*

fun ViewWriter.iosTest() {
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

        col {
            h2 { content = "Dynamic List" }
            val countString = Property("5")
            row {
                forEachUpdating(
                    shared {
                        (1..(countString.await().toIntOrNull() ?: 1).coerceAtMost(100)).map { "Item $it" }
                    }
                ) {
                    text { ::content.invoke { it.await() } }
                }
                space() in weight(1f)
            } in scrollsHorizontally
//            label {
//                content = "Element count:"
//                textField { content bind countString }
//            }
        } in card

        space() in weight(1f)
    }
}

fun sampleText() = "From Kotlin for iOS"