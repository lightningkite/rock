package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class Dashboard: RockScreen {
    override fun ViewContext.render() {
        text {
            content = "Dashboard"
        }
    }

    override fun createPath(): String = PATH

    companion object {
        const val PATH = "/dashboard"
    }
}
