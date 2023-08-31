package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class TestComponent() : RockScreen {
    override fun ViewContext.render() {
        text { content = "TEST COMPONENT" }
    }

    override fun createPath(): String = "/test"

    companion object {
        const val PATH = "/test"
    }
}
