package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

fun ViewContext.userSettings(props: RouteProps) {
    text { content = "The user id is: ${ props["userId"] ?: "" } and this is the settings page" }
}
