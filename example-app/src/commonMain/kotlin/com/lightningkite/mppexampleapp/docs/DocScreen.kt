package com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.docs

import com.lightningkite.rock.Routable
import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.models.FontAndStyle
import com.lightningkite.rock.models.systemDefaultFixedWidthFont
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

interface DocScreen: RockScreen {
    val covers: List<String>
}

val ViewWriter.code: ViewWrapper get() = tweakTheme {
    it.copy(body = FontAndStyle(font = systemDefaultFixedWidthFont))
}

@Routable("docs/screen")
object DataScreen: DocScreen {
    override val covers: List<String> = listOf(
        "data",
        "Property",
        "PersistentProperty",
        "shared",
        "reactiveScope",
        "::prop { }",
        "launch"
    )

    override fun ViewWriter.render() {
        col {
            h1("Data")
            text("Rock is roughly based on Solid.js, which uses smaller blocks to contain actions that should run when dependencies change.")

            text("However, to begin using that, we have to first look at Property.")
            code - text("val counter = Property<Int>(0)")
            val counter = Property<Int>(0)

            text("A Property is a changing value that we want to observe changes on.  We can now manipulate the value freely:")
            code - text("counter.value = 0")
            counter.value = 0

            text("Anyone who listens to the property will be notified when the value changes.  Now, how do we observe it?  We can use what's called a reactive scope:")
            card - row {
                expanding - code - text("""
                    text {
                        reactiveScope { content = "The current counter value is ${'$'}{counter.await()}" }
                    }
                """.trimIndent())
                separator()
                expanding - stack {
                    text {
                        reactiveScope { content = "The current counter value is ${counter.await()}" }
                    }
                }
            }

            text("Now, a button to increment it:")
            card - row {
                expanding - code - text("""
                important - button {
                    text("Increment the counter")
                    onClick { counter.value++ }
                }
                """.trimIndent())
                separator()
                expanding - stack {
                    important - button {
                        text("Increment the counter")
                        onClick { counter.value++ }
                    }
                }
            }
        }
    }

}