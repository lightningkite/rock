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
            example("""
                text
            """.trimIndent()) {
                card - sizedBox(SizeConstraints(width = 6.rem, height = 6.rem)) - col {
                            text { content = "Test" } in gravity(Align.Center, Align.Center)
                }
            }



        }
    }
}