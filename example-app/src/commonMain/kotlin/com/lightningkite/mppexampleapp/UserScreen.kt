package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class UserScreen(val userId: Int) : RockScreen {
    override fun ViewContext.render() = column {
        nativeText { content = "The user id is: $userId" }
        nativeButton {
            onClick {
                navigator.navigate(UserSettings(userId))
            }
            nativeText { content = "Settings" }
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
