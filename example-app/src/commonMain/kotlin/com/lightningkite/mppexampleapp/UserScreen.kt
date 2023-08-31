package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class UserScreen(val userId: Int) : RockScreen {
    override fun ViewContext.render() = column {
        text { content = "The user id is: $userId" }
        button {
            onClick {
                navigator.navigate(UserSettings(userId))
            }
            text { content = "Settings" }
        }
    }

    override fun createPath(): String = "/users/$userId"

    companion object {
        const val PATH = "/users/{userId}"
        fun create(props: RouteProps) = UserScreen(
            userId = props["userId"]!!.toInt()
        )
    }
}
