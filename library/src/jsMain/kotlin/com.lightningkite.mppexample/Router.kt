package com.lightningkite.mppexample

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.PopStateEvent

actual object RockNavigator {
    actual var router: Router? = null
        set(value) {
            if (field != null) throw Error("Attempted reinitialization of RockNavigator")
            field = value
            navigate(currentPath, pushState = false)
            window.addEventListener("popstate", { event ->
                event as PopStateEvent
                println("Intercepted navigation to $currentPath")
                navigate(currentPath, pushState = false)
            })
        }

    actual var currentPath: String
        get() = window.location.pathname
        set(value) {}

    actual fun navigate(path: String, pushState: Boolean) {
        if (router == null) throw Error("Uninitialized RockNavigator")
        println("Navigating to $path")
        document.body?.innerHTML = ""
        if (pushState)
            window.history.pushState(
                mapOf(
                    "path" to path
                ), "", path
            )
        router!!.render(path)
    }
}
