package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class NotFound() : RockScreen {
    override fun ViewContext.render() {
        text("You have reached the not found page")
    }

    override fun createPath(): String = "/test"

    companion object {
        const val PATH = "/test"
    }
}
