package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

fun ViewContext.userComponent(props: UserProps) {
    column {
        text { content = "The user id is: ${props.userId}" }
        button {
            onClick {
                RockNavigator.navigate("/users/${props.userId}/settings")
            }
            text { content = "Settings" }
        }
    }
}
