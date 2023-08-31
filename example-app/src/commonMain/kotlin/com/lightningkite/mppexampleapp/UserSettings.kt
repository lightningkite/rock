package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class UserSettings(val userId: Int) : RockScreen {
    override fun ViewContext.render() {
        nativeText { content = "The user id is: $userId and this is the settings page" }
    }

    override fun createPath(): String = "/users/$userId/settings"

    companion object {
        const val PATH = "/users/{userId}/settings"
        fun create(props: RouteProps) = UserSettings(userId = props["userId"]!!.toInt())
    }
}
