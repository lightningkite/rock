package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class NotFound() : RockScreen {
    override fun ViewContext.render() {
        nativeText { content = "404 NOT FOUND" }
    }

    override fun createPath(): String = "/test"

    companion object {
        const val PATH = "/test"
    }
}
