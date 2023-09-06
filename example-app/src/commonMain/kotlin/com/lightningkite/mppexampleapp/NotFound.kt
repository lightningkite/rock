package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class NotFound() : RockScreen {
    override fun ViewContext.render() {
        nativeText { content = "404 NOT FOUND" }
    }

    override fun createPath(): String = "/test"
    override val icon = null
    override val title = "Not Found"
    override val showInNavigation = false

    companion object {
        const val PATH = "/test"
    }
}
