package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*
import kotlinx.browser.document

fun main() {
    val context = ViewContext(document.body!!)
    context.elementToDoList.add {
        style.position = "absolute"
        style.left = "0px"
        style.top = "0px"
        style.right = "0px"
        style.bottom = "0px"
    }
    RockNavigator.router = Router(
        context,
        routes = listOf(
            Route("/") { homeComponent(it) },
            Route("/test") { testComponent(it) },
            Route("/users") { testComponent(it) },
            Route("/users/{userId}") { userComponent(it) },
            Route("/users/{userId}/settings") { userSettings(it) },
        )
    ) {
        text { content = "Not found" }
    }
}
