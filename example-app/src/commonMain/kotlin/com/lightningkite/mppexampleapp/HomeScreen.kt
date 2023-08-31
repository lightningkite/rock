package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class HomeScreen : RockScreen {
    override fun ViewContext.render() {
        column {
            val userIdProp = Property("1")

            text { content = "HOME COMPONENT" }
            button {
                onClick {
                    navigator.navigate(TestComponent())
                }
                text { content = "Navigate" }
            }

            textField {
                hint = "User Id"
                bind(userIdProp)
            } in margin(
                Insets(
                    top = 2.rem, bottom = 2.rem
                )
            )

            button {
                onClick {
                    navigator.navigate(UserScreen(userId = userIdProp.once.toInt()))
                }
                text { content = "Users" }
            } in margin(Insets(bottom = 2.rem))

            button {
                onClick {
                    navigator.navigate(NonexistentScreen())
                }
                text { content = "404" }
            }
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
