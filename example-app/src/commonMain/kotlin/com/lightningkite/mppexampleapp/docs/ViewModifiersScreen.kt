package com.lightningkite.mppexampleapp.docs

import com.lightningkite.rock.Routable
import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

@Routable("docs/view-modifiers")
object ViewModifiersScreen: DocScreen {
    override val covers: List<String> =
        listOf("view modifiers", "View modifier", "ViewModifiers", "viewModifiers", "viewmodifiers")

    override fun ViewWriter.render() {
        article {
            h1("View Modifiers")
            text("Rock has a number of view modifiers that can be used to modify the look, behavior and position of views.")
            h2("Gravity")
            text("Gravity has two parameters, horizontal and vertical.  It can be used to align items within a column, row or stack. " +
                    "You can use the following values for the two parameters ${
                        Align.entries.joinToString(", ") { it.name }
                    } ")
            text("Using the gravity modifier, you can align items within a column.")
            example("""
                    card  - col {
                    val aligns = listOf(Align.Start, Align.Center, Align.End)
                    for (h in aligns) {
                        for (v in aligns) {
                            text { content = "$\h $\v" } in gravity(h, v)
                        }
                    }
                } in sizedBox(SizeConstraints(minHeight = 200.px))
            """.trimIndent()) {
                card  - col {
                    val aligns = listOf(Align.Start, Align.Center, Align.End)
                    for (h in aligns) {
                        for (v in aligns) {
                            text { content = "$h $v" } in gravity(h, v)
                        }
                    }
                } in sizedBox(SizeConstraints(minHeight = 200.px))
            }
            h2("Weight")
            example("""
                
            """.trimIndent()){

            }
            h2("Text Popover")
            h2("Has Popover")

            h2("Scrolls")

            h2("Scrolls Horizontally")


            h2("Sized Box")

            h2("Size Constraints")

            h2("Marginless")

            h2("Padded")

            h2("Only When")


        }
    }
}