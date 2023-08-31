package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

interface Screen {
    fun ViewContext.render()
}

class UserScreen(val userId: Int): Screen {
    override fun ViewContext.render() = column {
        text { content = "The user id is: ${userId}" }
        button {
            onClick {
                RockNavigator.navigate("/users/${userId}/settings")
            }
            text { content = "Settings" }
        }
    }
}
