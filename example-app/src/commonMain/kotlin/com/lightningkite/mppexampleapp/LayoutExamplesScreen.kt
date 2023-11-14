package com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.ViewContext
import com.lightningkite.rock.views.card
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.setTheme

@Routable("layout-examples")
object LayoutExamplesScreen : RockScreen {
    override fun ViewContext.render() {
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
                        text { content = "$v" } in gravity(Align.Stretch, v)
                    }
                    stack { text { content = "Weight 1" } in gravity(Align.Center, Align.Center) } in weight(1f)
                    for (v in aligns) {
                        text { content = "$v" } in gravity(Align.Stretch, v)
                    }
                } in sizedBox(SizeConstraints(minHeight = 200.px))
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
                } in scrollsHorizontally
                label {
                    content = "Element count:"
                    textField { content bind countString }
                }
            } in card
        } in scrolls
    }
}

fun ViewContext.componentDemo() {
    val currentTheme = Property<Theme>(MaterialLikeTheme())

    col {


        col {
            h2 { content = "Sample Form" }
            h3 { content = "Without cards" }
            label { content = "First Name"; textField { hint = "Bill" } }
            label { content = "Last Name"; textField { hint = "Murray" } }
            label { content = "Password"; textField { this.keyboardHints = KeyboardHints.password } }
            h3 { content = "With cards" }
            label { content = "First Name"; textField { hint = "Bill" } in card }
            label { content = "Last Name"; textField { hint = "Murray" } in card }
            label { content = "Password"; textField { this.keyboardHints = KeyboardHints.password } in card }
        } in card

    } in scrolls in setTheme { currentTheme.await() } in marginless

}
