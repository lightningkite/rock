package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

fun ViewContext.homeComponent(route: RouteProps) {
    column {
        val userIdProp = Property("1")

        text { content = "HOME COMPONENT" }
        button {
            onClick {
                println("button click")
                navigator.navigate("/test")
            }
            text { content = "Navigate" }
        }

        textField {
            hint = "User Id"
            bind(userIdProp)
        } in margin(
            Insets(
                top = 2.rem,
                bottom = 2.rem
            )
        )

        button {
            onClick {
                println("button click")
                navigator.navigate("/users/${userIdProp.once}")
            }
            text { content = "Users" }
        } in margin(Insets(bottom = 2.rem))

        button {
            onClick {
                navigator.navigate("/404")
            }
            text { content = "404" }
        }
    }
}
