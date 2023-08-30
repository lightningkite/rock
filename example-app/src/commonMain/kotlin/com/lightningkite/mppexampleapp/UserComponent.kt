package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

fun ViewContext.userComponent(props: RouteProps) {
    text { content = "The user id is: ${ props["userId"] ?: "" }" }
}
