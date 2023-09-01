package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class HomeScreen : RockScreen {
    override fun ViewContext.render() {
        val counter = Property(0)
        launch {
            while (true) {
                delay(1000)
                counter.modify { it + 1 }
                async {
                    delay(200)
                }
            }
        }

        column {
            val userIdProp = Property("1")

            nativeText {
                content = "HOME COMPONENT"
            } in changingBackground { Background(fill = Color.gray(1 - counter.current / 20f)) }
            nativeButton {
                onClick {
                    navigator.navigate(TestComponent())
                }
                nativeText { content = "Navigate" }
            }

            text {
                ::content { "Counter: ${counter.current}" }
            }

            button {
                ::disabled { (counter.current / 5) % 2 == 0 }
                onClick {
                    println("Nice button clicked")
                }
                text {
                    content = "Nice Button"
                }
            }

            nativeTextField {
                hint = "User Id"
                bind(userIdProp)
            } in margin(
                Insets(
                    top = 2.rem, bottom = 2.rem
                )
            )

            nativeButton {
                onClick {
                    navigator.navigate(UserScreen(userId = userIdProp.once.toInt()))
                }
                nativeText { content = "Users" }
            } in margin(Insets(bottom = 2.rem))

            nativeButton {
                onClick {
                    navigator.navigate(NonexistentScreen())
                }
                nativeText { content = "404" }
            } in margin(Insets(bottom = 2.rem))

            nativeText {
                content = "hover me"
            } in interactive(
                transitions = false,
                hoverElevation = 8.px,
                hoverBackground = Background(
                    fill = LinearGradient(
                        angle = Angle(0.15f),
                        stops = listOf(
                            GradientStop(ratio = 0.5f, color = Color.yellow),
                            GradientStop(ratio = 1f, color = Color.red)
                        )
                    ),
                    stroke = Color.green,
                    strokeWidth = 4.px
                )
            )
        }
    }

    override fun createPath(): String = "/"

    companion object {
        const val PATH = "/"
    }
}

class NonexistentScreen : RockScreen {
    override fun ViewContext.render() {
        TODO("Not yet implemented")
    }

    override fun createPath(): String = "/404"

}
